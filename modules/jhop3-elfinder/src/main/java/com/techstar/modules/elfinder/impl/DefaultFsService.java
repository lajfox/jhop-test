package com.techstar.modules.elfinder.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.elfinder.localfs.LocalFsVolume;
import com.techstar.modules.elfinder.service.FsItem;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceConfig;
import com.techstar.modules.elfinder.service.FsVolume;
import com.techstar.modules.elfinder.util.LetterUtils;

public class DefaultFsService implements FsService {

	FsServiceConfig _serviceConfig = new DefaultFsServiceConfig();

	public FsServiceConfig getServiceConfig() {
		return _serviceConfig;
	}

	public void setServiceConfig(FsServiceConfig serviceConfig) {
		_serviceConfig = serviceConfig;
	}

	Map<FsVolume, String> _volumeIds = new HashMap<FsVolume, String>();

	FsVolume[] _volumes;

	String[][] escapes = { { "+", "_P" }, { "-", "_M" }, { "/", "_S" }, { ".", "_D" }, { "=", "_E" } };

	@Override
	public FsItem fromHash(String hash) {
		for (FsVolume v : _volumes) {
			String prefix = getVolumeId(v) + "_";

			if (hash.equals(prefix)) {
				return v.getRoot();
			}

			if (hash.startsWith(prefix)) {
				String localHash = hash.substring(prefix.length());

				for (String[] pair : escapes) {
					localHash = localHash.replace(pair[1], pair[0]);
				}

				String relativePath = new String(Base64.decodeBase64(localHash));
				return v.fromPath(relativePath);
			}
		}

		return null;
	}

	@Override
	public String getHash(FsItem item) {
		String relativePath = item.getVolume().getPath(item);
		String base = new String(Base64.encodeBase64(relativePath.getBytes()));

		for (String[] pair : escapes) {
			base = base.replace(pair[0], pair[1]);
		}

		return getVolumeId(item.getVolume()) + "_" + base;
	}

	@Override
	public String getVolumeId(FsVolume volume) {
		return _volumeIds.get(volume);
	}

	public FsVolume[] getVolumes() {
		return _volumes;
	}

	public void setVolumes(FsVolume[] volumes) {
		_volumes = volumes;

		int i = 0;
		String volumeId = null;
		for (FsVolume volume : volumes) {			
			volumeId = ((LocalFsVolume) volume).getVolumeId();
			if (StringUtils.isEmpty(volumeId)) {
				volumeId = LetterUtils.getLetter(i);
			} 
			_volumeIds.put(volume, volumeId);
		}
	}

}
