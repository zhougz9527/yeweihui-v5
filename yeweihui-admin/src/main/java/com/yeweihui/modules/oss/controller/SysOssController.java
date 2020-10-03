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

package com.yeweihui.modules.oss.controller;

import com.yeweihui.common.utils.ConfigConstant;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.common.validator.group.QcloudGroup;
import com.yeweihui.common.validator.group.QiniuGroup;
import com.yeweihui.modules.oss.cloud.CloudStorageConfig;
import com.yeweihui.modules.oss.cloud.OSSFactory;
import com.yeweihui.modules.oss.entity.SysOssEntity;
import com.yeweihui.modules.oss.entity.TokenDomainEntity;
import com.yeweihui.modules.oss.service.SysOssService;
import com.google.gson.Gson;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.validator.group.AliyunGroup;
import com.yeweihui.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 12:13:26
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}


    /**
     * 云存储配置信息
     */
    @RequestMapping("/config")
//    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@RequestMapping("/saveConfig")
//	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
//	@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		String url = sysOssService.upload(file);
		if (StringUtils.isNotEmpty(url)) {
			return R.ok().put("url", url);
		} else {
			return R.error("url为空");
		}
//		return R.error("url 为空");
		
	}


	/**
	 * 删除
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids){
		sysOssService.deleteBatchIds(Arrays.asList(ids));

		return R.ok();
	}

	/**
	 * 获取token和domain，七牛云上传用
	 * @return
	 */
	@RequestMapping("/getTokenDomain")
//	@RequiresPermissions("sys:oss:all")
	public R getTokenDomain(){
		TokenDomainEntity tokenDomainEntity = OSSFactory.build().getTokenDomain();
		return R.ok().put("tokenDomain", tokenDomainEntity);
	}
}
