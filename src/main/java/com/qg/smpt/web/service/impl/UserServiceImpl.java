package com.qg.smpt.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.PrinterMapper;
import com.qg.smpt.web.repository.UserMapper;
import com.qg.smpt.web.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private static final Logger lOGGER = Logger.getLogger(UserServiceImpl.class);
	
	
	@Autowired
	private UserMapper userMapper;	
	
	@Autowired
	private PrinterMapper printerMapper;
	
	/**
	 * 根据用户id获取用户
	 * @param userId
	 * @return 存在则返回对应用户,不存在则返回空
	 */
	public User queryById(int userId) {
		lOGGER.log(Level.DEBUG, "正在通过主键[{0}]来查找用户", userId);
		
		User user = null;
		try{
			user = userMapper.selectByPrimaryKey(userId);
		}catch(Exception e) {
			lOGGER.log(Level.ERROR, "通过主键[{0}]来查找用户出错了", userId,e);
		} 
		
		return user;
	}
	
	/**
	 * 查询所有用户
	 * @return 返回对应的用户集合
	 */
	public List<User> queryAllUser() {
		List<User> users = null;
		try{
			users = userMapper.selectAllUser();
		}catch(Exception e) {
			lOGGER.log(Level.ERROR, "UserService.queryAllUser 查询所有用户出错 ", e);
		}
		
		return users;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public String registerUser(User user) {
		try{
			//执行插入用户
			int userId = userMapper.insert(user);
			
			List<Printer> printers = user.getPrinters();
			for(Printer p : printers) {
				p.setUserId(userId);
				p.setPrinterStatus(String.valueOf((int)(Constant.PRINTER_HEATHY)));
			}
			
			printerMapper.insertPrinterBatch(printers);
			printerMapper.addUserPrinterBatch(printers);
			
			return Constant.SUCCESS;
		}catch(Exception e) {
			lOGGER.log(Level.ERROR, "注册用户失败了", e);
			
			return Constant.ERROR;
		}
	}
	
	public int login() {
		return 0;
	}
}