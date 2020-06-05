
package org.magnum.dataup;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoWrapper;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MeraController {

	@Autowired
	private VideoWrapper videoWrapper;
	private VideoFileManager videoFileManager;
	
	@RequestMapping(value = "/video", method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v){
		// Do something with the Video
		// set id
		v.setId(videoWrapper.generateId());
		
		//System.out.println("inside post video method id generated");
		
		v.setDataUrl(getUrlBaseForLocalServer() + "/video/" + v.getId() + "/data");
		videoWrapper.addVideo(v);
		
		
		return v;
	}
	
	@RequestMapping(value = "/video", method = RequestMethod.GET)
	public @ResponseBody List<Video> getVideo(){
		// Do something with the Video
		// ...
		//System.out.println("inside get video method");


		return videoWrapper.getAllVideos();
	}
	
	@RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
	public @ResponseBody VideoStatus addOneVideo(@RequestParam("data") MultipartFile file,@PathVariable("id")long id) throws VideoNotCreatedException  {
		// Do something with the Video
		// ...
		
		try {
			videoFileManager=VideoFileManager.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Video v=videoWrapper.getVideoById(id);
		if (v==null){
			String message="Video not created yet";
			throw new VideoNotCreatedException(message);
			
		}else{
		//System.out.println("inside post method. video id:"+id+" name: "+v.getTitle());
		try {
			videoFileManager.saveVideoData(v, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VideoStatus videoStatus = new VideoStatus(VideoState.READY);
		return videoStatus;}
	}
	
	@RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
	public @ResponseBody void getOneVideo(@PathVariable long id, HttpServletResponse response) throws IOException{
		// Do something with the Video
		// ...
		Video v=videoWrapper.getVideoById(id);
		if (v==null){
			String message="Video not created yet";
			throw new VideoNotCreatedException(message);
			
		}else{
		videoFileManager.copyVideoData(v, response.getOutputStream());
		
		
		return ;}
	}
	
	
	private String getUrlBaseForLocalServer() {
		   HttpServletRequest request = 
		       ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		   String base = 
		      "http://"+request.getServerName() 
		      + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
		   return base;
		}
	
}
