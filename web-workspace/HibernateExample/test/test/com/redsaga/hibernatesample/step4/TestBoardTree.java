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
public class TestBoardTree extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUserCRUD.class);
	}

	public void setUp() throws HibernateException
	{
		_BaseRootDAO.initialize();
	}
	
	public void testBoardTree() throws HibernateException
	{
		ForumService fs = ForumServiceFactory.getHibernateForumService();
		
		User shark = new User();
		shark.setName("Shark");
		shark.setPwd("guessme");
		fs.saveUser(shark);
		
		Board a = new Board();
		a.setCreateBy(shark);
		a.setName("A");
		fs.addBoard(a);
		
		Board b = new Board();
		b.setCreateBy(shark);
		b.setName("B");
		fs.addBoard(b);
		
		Board a1 = new Board();
		a1.setCreateBy(shark);
		a1.setName("A1");
		fs.addChildBoard(a,a1);
		
		Board a11 = new Board();
		a11.setCreateBy(shark);
		a11.setName("A11");
		fs.addChildBoard(a1,a11);
		
		Board a12 = new Board();
		a12.setCreateBy(shark);
		a12.setName("A12");
		fs.addChildBoard(a1,a12);
		
		Board b1 = new Board();
		b1.setCreateBy(shark);
		b1.setName("B1");
		b1.setParent(b);
		fs.addChildBoard(b,b1);
		
		List boardList = fs.getBoardList();
		
		Session session = RootDAO.createSession();
		String boardTree = printBoardTree(boardList);
		RootDAO.getInstance().closeSession();
		
		fs.deleteBoard(b);
		fs.deleteBoard(a);
		
		fs.deleteUser(shark);

		assertEquals("[A]([A1]([A11][A12]))[B]([B1])",boardTree);
		
	}
	
	protected String printBoardTree(Collection nodes) throws HibernateException
	{
		Session session = _RootDAO.createSession();
		
		String result = "";
		Iterator it = nodes.iterator();
		while(it.hasNext())
		{
			Board board = (Board) it.next();
			session.lock(board,LockMode.READ);
			result += "["+board.getName()+"]";
			Collection children = board.getChildBoards();
			if (children.size()>0)
				result += "("+printBoardTree(children)+")";
		}
		return result;
	}

}
