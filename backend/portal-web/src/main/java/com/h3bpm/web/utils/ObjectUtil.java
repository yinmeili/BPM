package com.h3bpm.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.h3bpm.web.vo.OrgInfoVo;

public class ObjectUtil {
   
	/**
     * 序列化
     *
     * @param o
     * @return
     * @throws IOException
     */
    public static byte[] persistenceObject(Object o) throws IOException {
    	if(o == null){
    		return null;
    	}
    	
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutput objOutput = new ObjectOutputStream(byteOutput);
        objOutput.writeObject(o);
        return byteOutput.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bt
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object unPersistenceObject(byte[] bt) throws IOException, ClassNotFoundException {
    	if(bt == null){
    		return null;
    	}
        ObjectInput objInput = new ObjectInputStream(new ByteArrayInputStream(bt));
        return objInput.readObject();
    }

    
	
	@SuppressWarnings("unchecked")
	@Autowired
	public static void main(String[] args){
		List<OrgInfoVo> orgInfoList = new ArrayList<OrgInfoVo>();
		for(int i = 0; i<=100; i++){
			OrgInfoVo org = new OrgInfoVo();
			org.setId("aaaaaaaaaaaaaaaaaaaaa"+i);
			org.setName("哒哒哒哒哒哒多多多多多多"+i);

			orgInfoList.add(org);
		}



		try {
			System.out.println(ObjectUtil.persistenceObject(orgInfoList));

			try {
				List<OrgInfoVo> b = (List<OrgInfoVo>) ObjectUtil.unPersistenceObject(ObjectUtil.persistenceObject(orgInfoList));

				b.size();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
