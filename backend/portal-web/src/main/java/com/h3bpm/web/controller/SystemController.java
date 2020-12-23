package com.h3bpm.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.vo.DictionaryDataVo;
import com.h3bpm.web.vo.ResponseVo;

import OThinker.H3.Entity.Data.Metadata.EnumerableMetadata;
import OThinker.H3.Entity.Data.Metadata.IMetadataRepository;

@Controller
@RequestMapping(value = "/Portal/system")
public class SystemController extends AbstractController {

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/findDictionaryData", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo findDictionaryData(String categoryCode) throws Exception {
		IMetadataRepository metadataRepository = this.getEngine().getMetadataRepository();
		List<EnumerableMetadata> metadataList = metadataRepository.GetByCategory(categoryCode);

		List<DictionaryDataVo> dictionaryDataVos = new ArrayList<DictionaryDataVo>();
		if (metadataList != null) {
			for (EnumerableMetadata metadata : metadataList) {
				dictionaryDataVos.add(new DictionaryDataVo(metadata));
			}
		}

		return new ResponseVo(dictionaryDataVos);
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
