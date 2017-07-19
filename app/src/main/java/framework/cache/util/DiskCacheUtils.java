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
package framework.cache.util;

import android.content.Context;


import java.io.File;
import java.io.IOException;

import framework.cache.disk.DiskCache;
import framework.cache.disk.impl.BaseDiskCache;
import framework.cache.disk.impl.ext.LruDiskCache;
import framework.cache.disk.naming.FileNameGenerator;
import framework.cache.disk.naming.HashCodeFileNameGenerator;

/**
 * Utility for convenient work with disk cache.<br />
 * <b>NOTE:</b> This utility works with file system so avoid using it on
 * application main thread.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public final class DiskCacheUtils {

	private DiskCacheUtils() {
	}

	/**
	 * Creates {@linkplain HashCodeFileNameGenerator default implementation} of
	 * FileNameGenerator
	 */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/**
	 * Creates default implementation of {@link DiskCache} depends on incoming
	 * parameters
	 * @param diskCacheSize 可使用磁盘最大大小(单位byte,字节)
	 */
	public static DiskCache createDiskCache(Context context,
											FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
											int diskCacheFileCount) {
		File reserveCacheDir = createReserveDiskCacheDir(context);
		if (diskCacheSize > 0 || diskCacheFileCount > 0) {
			File individualCacheDir = StorageUtils
					.getIndividualCacheDirectory(context);
			try {
				return new LruDiskCache(individualCacheDir, reserveCacheDir,
						diskCacheFileNameGenerator, diskCacheSize,
						diskCacheFileCount);
			} catch (IOException e) {
				//LazyLogger.e(e, "获取LruDiskCache错误");
				// continue and create unlimited cache
			}
		}
		File cacheDir = StorageUtils.getCacheDirectory(context);
		return new BaseDiskCache(cacheDir, reserveCacheDir,
				diskCacheFileNameGenerator);
	}

	/**
	 * Creates reserve disk cache folder which will be used if primary disk
	 * cache folder becomes unavailable
	 */
	private static File createReserveDiskCacheDir(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context, false);
		File individualDir = new File(cacheDir, "uil-cache");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return cacheDir;
	}

	/**
	 * Returns {@link File} of cached key or <b>null</b> if key was not cached
	 * in disk cache
	 */
	public static File findInCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() ? file : null;
	}

	/**
	 * Removed cached key file from disk cache (if key was cached in disk cache
	 * before)
	 *
	 * @return <b>true</b> - if cached key file existed and was deleted;
	 *         <b>false</b> - otherwise.
	 */
	public static boolean removeFromCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() && file.delete();
	}
}
