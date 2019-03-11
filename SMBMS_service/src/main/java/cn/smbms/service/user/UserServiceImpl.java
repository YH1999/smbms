package cn.smbms.service.user;

import cn.smbms.tools.RedisTemplateUtil;
import org.springframework.stereotype.Service;
import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private RedisTemplateUtil redisTemplateUtil;

	@Override
	public User login(String userCode, String userPassword) throws Exception {
		// TODO Auto-generated method stub
		User u= (User) redisTemplateUtil.get(userCode);
		if(u!=null){
			return u;
		}else{
			User user = null;
			System.out.println(userCode+"---------------------------------------+"+userPassword);
			user = userMapper.getLoginUser(userCode);
			redisTemplateUtil.set(userCode,user);
			System.out.println(user.getUserPassword()+"---------------------------------------");
			//匹配密码
			if(null != user){
				if(!user.getUserPassword().equals(userPassword)){
					user = null;}
			}
			return user;
		}

	}

	@Override
	public boolean add(User user) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(userMapper.add(user) > 0){
			flag = true;}
		return flag;
	}

	@Override
	public List<User> getUserList(String queryUserName, Integer queryUserRole,
                                  Integer currentPageNo, Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		currentPageNo = (currentPageNo-1)*pageSize;
		return userMapper.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
	}

	@Override
	public int getUserCount(String queryUserName, Integer queryUserRole)
			throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserCount(queryUserName, queryUserRole);
	}

	@Override
	public User selectUserCodeExist(String userCode) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getLoginUser(userCode);
	}

	@Override
	public boolean deleteUserById(Integer delId) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = true;
		//先删除该条记录的上传附件
		User user = userMapper.getUserById(delId);
		if(user.getIdPicPath() != null && !user.getIdPicPath().equals("")){
			//删除服务器上个人证件照
			File file = new File(user.getIdPicPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag && user.getWorkPicPath() != null && !user.getWorkPicPath().equals("")){
			//删除服务器上个人工作证照片
			File file = new File(user.getWorkPicPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag){
			if(userMapper.deleteUserById(delId) < 1){
				flag = false;}
		}
		return flag;
	}

	@Override
	public User getUserById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserById(id);
	}

	@Override
	public boolean modify(User user) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(userMapper.modify(user) > 0){
			flag = true;}
		return flag;
	}

	@Override
	public boolean updatePwd(Integer id, String pwd) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(userMapper.updatePwd(id, pwd) > 0){
			flag = true;}
		return flag;
	}

}
