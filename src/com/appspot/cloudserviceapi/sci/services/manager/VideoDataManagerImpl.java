package com.appspot.cloudserviceapi.sci.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sci.dao.VideoDataDAO;

import tapp.model.sci.VideoData;

public class VideoDataManagerImpl implements VideoDataManager {

//	private List<User> myBeans = new ArrayList<User>();
	private List<VideoData> myBeans = (new VideoDataDAO()).getCloneList();

	public List<VideoData> getVideoDatas() {
//		return myBeans;
		return (new VideoDataDAO()).getCloneList();
	}

	public void setVideoDatas(List<VideoData> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		VideoData myBean = getVideoData(id);
		getVideoDatas().remove(myBean);
		(new VideoDataDAO()).remove(id);
	}

	public void save(VideoData myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new VideoDataDAO()).save(myBean);
	}

	public VideoData getVideoData(Long id) {
		VideoData retVal = null;
//		for (VideoData myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new VideoDataDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}