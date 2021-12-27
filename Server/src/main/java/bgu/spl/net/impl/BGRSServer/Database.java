package bgu.spl.net.impl.BGRSServer;

import java.io.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

	private ConcurrentHashMap<String, User> users;
	private ConcurrentHashMap<Integer, CourseInfo> courses;
	private ComparatorByCourses comparator;
	private Object regLock;
	private Object logLock;
	private Object courseLock;


	private Database() {
		users = new ConcurrentHashMap<>();
		courses = new ConcurrentHashMap<>();
		initialize("Courses.txt");
		regLock = new Object();
		logLock = new Object();
		courseLock = new Object();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class DatabaseHolder{
		private static Database instance = new Database();
	}
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		try {
			FileReader input = new FileReader(coursesFilePath);
			BufferedReader bufferedReader = new BufferedReader(input);
			String line;
			LinkedList list = new LinkedList();

			while ((line = bufferedReader.readLine()) != null) {
				String[] arr1 = line.split("\\|");

				int num = Integer.parseInt(arr1[0]);
				list.addLast(num);
				String name = arr1[1];
				int[] kdam;
				if (arr1[2].length() > 2)
					kdam = Stream.of(arr1[2].substring(1, arr1[2].length() - 1).split(",")).mapToInt(Integer::parseInt).toArray();
				else
					kdam = null;

				int cap = Integer.parseInt(arr1[3]);
				CourseInfo courseInfo = new CourseInfo((short)num, name,kdam, cap);
				courses.put(num, courseInfo);
			}
			comparator = new ComparatorByCourses(list);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


	}


	public boolean isLogin(String userName){
		synchronized (logLock) {
			User user = users.get(userName);
			return user.isLogin();
		}
	}

	public boolean login(String username, String pass) {
		synchronized (logLock) {
			if (isRegistered(username)) {
				if (!isLogin(username)) {
					User user = users.get(username);
					if (user.getPassword().equals(pass)) {
						user.setLogin(true);
						return true;
					}
				}
			}
			return false;
		}
	}

	public boolean logout(String username) {
		synchronized (logLock) {

			if (isRegistered(username)) {
				if (isLogin(username)) {
					User user = users.get(username);
					user.setLogin(false);
					return true;
				}
			}
			return false;
		}
	}

	public boolean isRegistered(String userName){
		synchronized (regLock) {
			return users.containsKey(userName);
		}
	}

	public boolean register(String userName, String pass, boolean admin){
		synchronized (regLock) {
			if (!isRegistered(userName)) {
				User user = new User(userName, pass, admin);
				users.put(userName, user);
				return true;
			}
			return false;
		}
	}

	public Integer[] getKdam(short courseNum) {
		CourseInfo course = courses.get((int)courseNum);
		short[] kdamArr = course.getCourseKdam();
		Integer[] kdamList = new Integer[kdamArr.length];
		for (int i = 0; i < kdamArr.length; i++)
			kdamList[i] = (int)kdamArr[i];
		Arrays.sort(kdamList, comparator);

		return  kdamList;
	}

	public boolean isCourseExist(short courseNum) {
		return courses.containsKey((int)courseNum);
	}

	public boolean isAdmin(String userName){
		User user = users.get(userName);
		return user.isAdmin();
	}

	public void addStudentToCourse(short courseNum, String userName){
		CourseInfo courseInfo = courses.get((int)courseNum);
		courseInfo.add(userName);
	}

	private boolean haveKdam(User user, short[] kdamList) {
		return user.haveKdam(kdamList);
	}

	public boolean registerToCourse(String userName, short courseNum){
		if (!isAdmin(userName)){
			User user = users.get(userName);
			if (isCourseExist(courseNum)) {
				CourseInfo course = courses.get((int) courseNum);
				synchronized (course) {
					if (!user.isRegtoCourse(courseNum))
						if (!isFull(course)) {
							if (course.getCourseKdam() != null) {
								short[] kdamList = course.getCourseKdam();
								if (haveKdam(user, kdamList)) {
									user.addCourse(courseNum);
									course.increaseNumOfStudents();
									addStudentToCourse(courseNum, userName);
									return true;
								}
							} else {
								user.addCourse(courseNum);
								course.increaseNumOfStudents();
								addStudentToCourse(courseNum, userName);
								return true;
							}
						}
				}
			}
		}
		return false;
	}

	public void unrgisterCourse(String userName, short courseNum) {
		User user = users.get(userName);
		CourseInfo course = courses.get((int)courseNum);
		synchronized (course) {
			course.removeStudent(userName);
		}

			user.removeCourse(courseNum);

	}

	private boolean isFull(CourseInfo course) {
		synchronized (course) {
			return course.isFull();
		}
	}

	public String getCourseStatus(short courseNum) {
		CourseInfo courseInfo = courses.get((int)courseNum);
		synchronized (courseInfo) {
			String output = "Course: (" + courseNum + ") " + courseInfo.getCourseName() + "\n"
					+ "Seats Available: " +(courseInfo.getCourseCapacity() - courseInfo.getNumOfStudents()) + "/" + courseInfo.getCourseCapacity() + "\n" +
					"Students Registered: [" + courseInfo.getRegStudents() + "]";
			return output;
		}
	}

	public String getStudentStatus(String userToCheck) {
		User user = users.get(userToCheck);
			String output = "Student: " + userToCheck + "\n"
					+ "Courses: [" + user.getCourses(comparator) + "]";
			return output;
	}

	public boolean isUserRegToCourse(String userName, short courseNum) {
		User user = users.get(userName);
		return user.isRegtoCourse(courseNum);
	}

	public String getCourseList(String userName) {
		User user = users.get(userName);
		return user.getCourses(comparator);
	}






}