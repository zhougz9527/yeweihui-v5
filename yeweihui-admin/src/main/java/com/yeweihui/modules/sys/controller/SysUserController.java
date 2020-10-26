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

package com.yeweihui.modules.sys.controller;


import com.yeweihui.common.annotation.SysLog;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.Assert;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.common.validator.group.AddGroup;
import com.yeweihui.common.validator.group.UpdateGroup;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.UserService;
import org.apache.catalina.User;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private UserService userService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	/*@RequiresPermissions("sys:user:list")*/
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = userService.queryPage(params);
		for (Object user : page.getList()) {
			String roleName = sysRoleService.getHighestLevelRoleNameByUserId(((UserEntity) user).getId());
			if (null != roleName) {
				((UserEntity) user).setRoleName(roleName);
			}
		}
		return R.ok().put("page", page);
	}

	/**
	 * 所有用户
	 */
	@RequestMapping("/all")
	public R all(){
		List<UserEntity> all = userService.selectByMap(new HashMap<>());
		return R.ok().put("all", all);
	}

	@PostMapping("/listByUser")
	public R simpleList(@RequestBody Map<String, Object> params){
		List<UserEntity> userEntityList = userService.listByUser(params);
		return R.ok().put("list", userEntityList);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		UserEntity user = getUser();
		String roleName = sysRoleService.getHighestLevelRoleNameByUserId(user.getId());
		if (null != roleName) {
			user.setRoleName(roleName);
		}
		return R.ok().put("user", user);
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");

		//原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
				
		//更新密码
		boolean flag = userService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		UserEntity user = userService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}

	/**
	 * 用户更多的信息
	 */
	@RequestMapping("/infoMore/{userId}")
	public R infoMore(@PathVariable("userId") Long userId){
		UserEntity user = userService.info(userId);
		String roleName = sysRoleService.getHighestLevelRoleNameByUserId(user.getId());
		if (null != roleName) {
			user.setRoleName(roleName);
		}
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody UserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

		userService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody UserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		userService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}

		userService.deleteBatchIds(Arrays.asList(userIds));
		
		return R.ok();
	}
}
