/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.framework.cache.disk.impl;


import com.framework.cache.LimitedAge;
import com.framework.cache.disk.DiskCache;
import com.framework.cache.disk.read.ReadFromDisk;
import com.framework.cache.disk.write.WriteInDisk;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件有过期时间的磁盘缓存类
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.3.1
 */
public class LimitedAgeDiskCache implements DiskCache {

	private DiskCache mDiskCache;
	private final Map<File, LimitedAge> loadingDates = Collections
			.synchronizedMap(new HashMap<File, LimitedAge>());

	private long maxAge;
	/**
	 * @param DiskCache
	 *            磁盘缓存类
	 * @param maxAge
	 *            缓存数据最大期限(单位秒)
	 */
	public LimitedAgeDiskCache(DiskCache diskCache, long maxAge) {
		this.mDiskCache = diskCache;
		this.maxAge = maxAge;
	}
	
	@Override
	public long size() {
		if (mDiskCache == null) {
			return 0;
		}
		return mDiskCache.size();
	}

	@Override
	public File getDirectory() {
		if (mDiskCache == null) {
			return null;
		}
		return mDiskCache.getDirectory();
	}

	@Override
	public File getFile(String key) {
		if (mDiskCache == null) {
			return null;
		}
		File file = mDiskCache.getFile(key);
		if (file != null && file.exists()) {
			boolean cached;
			LimitedAge loadingDate = loadingDates.get(file);
			if (loadingDate == null) {
				cached = false;
				loadingDate = new LimitedAge(file.lastModified(), maxAge);
			} else {
				cached = true;
			}

			if (loadingDate.checkExpire()) {
				loadingDates.remove(file);
				mDiskCache.remove(key);
				file.delete();
			} else if (!cached) {
				loadingDates.put(file, loadingDate);
			}
		}
		return file;
	}

	@Override
	public <V> V get(String key, ReadFromDisk<V> readFromDisk) {
		if (mDiskCache == null) {
			return null;
		}
		File file = getFile(key);
		if(file==null||!file.exists()){
			return null;
		}
		return mDiskCache.get(key, readFromDisk);
	}

	@Override
	public <V> boolean put(String key, WriteInDisk<V> writeIn, V value)
			throws IOException {
		if (mDiskCache == null) {
			return false;
		}
		boolean saved = mDiskCache.put(key, writeIn, value);
		if (loadingDates.get(key) == null) {
			rememberUsage(key, maxAge);
		}
		return saved;
	}

	@Override
	public <V> boolean put(String key, WriteInDisk<V> writeIn, V value,
                           long maxLimitTime) throws IOException {
		if (mDiskCache == null) {
			return false;
		}
		boolean saved = mDiskCache.put(key, writeIn, value, maxLimitTime);
		rememberUsage(key, maxLimitTime);
		return saved;
	}

	@Override
	public boolean remove(String key) {
		if (mDiskCache == null) {
			return false;
		}
		loadingDates.remove(getFile(key));
		return mDiskCache.remove(key);
	}

	@Override
	public void clear() {
		if (mDiskCache != null) {
			mDiskCache.clear();
		}
		loadingDates.clear();
	}

	private void rememberUsage(String key, long mMaxlimitTime) {
		File file = getFile(key);
		long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		loadingDates.put(file, new LimitedAge(currentTime, mMaxlimitTime));
	}

	@Override
	public void close() {
		if (mDiskCache != null) {
			mDiskCache.close();
			mDiskCache = null;
		}
		loadingDates.clear();
	}
}