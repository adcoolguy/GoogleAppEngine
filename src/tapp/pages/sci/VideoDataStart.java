package tapp.pages.sci;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.appspot.cloudserviceapi.sci.services.manager.VideoDataManager;

import tapp.model.sci.VideoData;

public class VideoDataStart {

	@Inject
	private VideoDataManager beanManager;

	private VideoData myBean;

    @Inject
    private BeanModelSource beanModelSource;
    
	public VideoData getVideoData() {
		return myBean;
	}

	public void setVideoData(VideoData VideoData) {
		this.myBean = VideoData;
	}

	public List<VideoData> getVideoDatas() {
		return beanManager.getVideoDatas();
	}

	public boolean isEmptyList() {
		return beanManager.getVideoDatas().isEmpty();
	}

	public void onActionFromDelete(Long id) {
		beanManager.delete(id);
	}
}
