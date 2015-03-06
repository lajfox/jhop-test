package com.techstar.modules.elfinder.service;


public interface FsService {
	FsItem fromHash(String hash);

	String getHash(FsItem item) ;

	String getVolumeId(FsVolume volume);

	FsVolume[] getVolumes();

	FsServiceConfig getServiceConfig();

}