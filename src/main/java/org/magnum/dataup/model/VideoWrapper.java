package org.magnum.dataup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class VideoWrapper {

	private List<Video> videoList;
	private int urlNumber;
	private List<Long> idList;
	private Random r;
	
	VideoWrapper(){
		videoList=new ArrayList<Video>();
		urlNumber=0;
		idList=new ArrayList<Long>();
		r=new Random();
	}
	public void addVideo(Video v){
		videoList.add(v);
	}
	
	public List<Video> getAllVideos(){
		return videoList;
	}
	public int geturlNumber(){
		urlNumber+=1;
		return urlNumber;
	}
	public boolean checkIfUniqueId(long id){
		if(id==0) return false;
		else return !(idList.contains(id));
	}
	
	public long generateId(){
		long id=r.nextLong();
		if(id<0)id=-id;
		while(!checkIfUniqueId(id)){
			id=r.nextLong();
		}
		return id;
		
	}
	public Video getVideoById(long id){
		Video reqVideo = null;
		for(Video v:videoList)
		{
			if(v.getId()==id){
				reqVideo=v;
				break;
			}
		}
		return reqVideo;
		
	}
}
