/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yeweihui.modules.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.enums.bfly.MagicEnum;
import com.yeweihui.modules.oss.cloud.OSSFactory;
import com.yeweihui.modules.oss.dao.SysOssDao;
import com.yeweihui.modules.oss.entity.SysOssEntity;
import com.yeweihui.modules.oss.service.SysOssService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Value("${aliyun.oss.accesskey}")
	String accessKey;
	@Value("${aliyun.oss.secret}")
	String secret;
	@Value("${aliyun.oss.endpoint}")
	String endpoint;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<SysOssEntity> page = this.selectPage(
				new Query<SysOssEntity>(params).getPage()
		);

		return new PageUtils(page);
	}

	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
    @Override
    public String upload(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}

		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		this.insert(ossEntity);
		return url;
    }

	/**
	 *
	 * @param multipartFile
	 * @return
	 */
	@Override
	public String upload(MultipartFile multipartFile, String bucket) {
		boolean isSuccess = false;
		OSSClient ossClient = null;
		String ossFilePath = "";
		File file = null;
		try {
			// 转换MultipartFile为File
			String fileName = multipartFile.getOriginalFilename();
			String prefix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			String newFileName = fileName + prefix;
			File dir = new File(System.getProperty("user.dir") + File.separator + "tmp");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = File.createTempFile(newFileName, prefix, dir);
			multipartFile.transferTo(file);
			//当前日期
			LocalDate date = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			ossFilePath = "file/" + date.format(formatter) + "/" + System.currentTimeMillis() + "_" + fileName;
			// 上传阿里云oss
			ossClient = new OSSClient(endpoint, accessKey, secret);
			logger.info("upload file to oss start");
			ossClient.putObject(bucket, ossFilePath, file);
			isSuccess = true;
			logger.info("upload file to oss end, ossFilePath: " + ossFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("upload file to oss error: " + e.getMessage());
		} finally {
			if (null != ossClient) {
				ossClient.shutdown();
			}
			if (null != file) {
				file.deleteOnExit();
			}
		}
		return isSuccess ? "https://ywh-bfly-comm.oss-cn-hangzhou.aliyuncs.com/" + ossFilePath : "";
	}

}
