package test.com.redsaga.hibernatesample.step4;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;

import com.redsaga.hibernatesample.step4.Article;
import com.redsaga.hibernatesample.step4.Board;
import com.redsaga.hibernatesample.step4.ForumService;
import com.redsaga.hibernatesample.step4.ForumServiceFactory;
import com.redsaga.hibernatesample.step4.User;
import com.redsaga.hibernatesample.step4.base._BaseRootDAO;
import com.redsaga.hibernatesample.step4.dao.RootDAO;
import com.redsaga.hibernatesample.step4.dao.UserDAO;
import com.redsaga.hibernatesample.step4.dao._RootDAO;

import junit.framework.TestCase;

/**
 * @author cao
 */
public class TestInterceptor extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUserCRUD.class);
	}

	public void setUp() throws HibernateException
	{
		_BaseRootDAO.initialize();
	}
	
	public void testInterceptor() throws HibernateException
	{
		ForumService fs = ForumServiceFactory.getHibernateForumService();
		
		//׼���û�
		User shark = new User();
		shark.setName("Shark");
		shark.setPwd("guessme");
		fs.saveUser(shark);
		
		//׼������
		Board board = new Board();
		board.setCreateBy(shark);
		board.setName("A");
		fs.addBoard(board);
		
		//��������
		Article root = new Article();
		root.setTitle("test");
		root.setLastUpdateTime(new Date());
		root.setLastUpdateBy(shark);
		root.setCreateBy(shark);
		fs.addNewPost(board,root);
		assertEquals(1,root.getNodeLevel());
		assertEquals("0001",root.getTreeIndex());

		//��������
		Article child = new Article();
		child.setTitle("test");
		child.setLastUpdateTime(new Date());
		child.setLastUpdateBy(shark);
		child.setCreateBy(shark);
		fs.replyPost(root,child);
		assertEquals(2,child.getNodeLevel());
		assertEquals("0001.0001",child.getTreeIndex());

		//��������2
		Article child2 = new Article();
		child2.setTitle("test");
		child2.setLastUpdateTime(new Date());
		child2.setLastUpdateBy(shark);
		child2.setCreateBy(shark);
		fs.replyPost(child,child2);
		
		assertEquals(3,child2.getNodeLevel());
		assertEquals("0001.0001.0001",child2.getTreeIndex());

		fs.deleteBoard(board);
		fs.deleteUser(shark);
	}

}
