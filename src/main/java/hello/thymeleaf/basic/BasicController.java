package hello.thymeleaf.basic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.Data;

@Controller
@RequestMapping("/basic")
public class BasicController {
	
	// 데이터 출력
	@GetMapping("text-basic")
	public String textBasic(Model model) {
		model.addAttribute("data", "Hello thymeleaf");
		model.addAttribute("unescape", "Hello <b>unescape</b>");
		return "basic/text-basic";
	}
	
	// 변수 사용
	@GetMapping("/variable")
	public String variable(Model model) {
		User userA = new User("userA", 10);
		User userB = new User("userB", 20);
		
		// list 는 user[0].usernmae 이렇게 index를 먼저 [] 안에 입력하기
		List<User> list = new ArrayList<>();
		list.add(userA);
		list.add(userB);
		
		// TODO: Map에 data 담아서 보내기
		// Map의 value에 객체를 넣으면 
		// 타임리프로 userMap['userA'].username 이렇게 꺼내면 됨 (키 먼저 [] 안에 입력하기)
		// value에 객체가 아닌 그냥 값을 넣으면
		// userMap.username 이렇게 꺼낼 수 있음
		Map<String, User> map = new HashMap<>();
		map.put("userA", userA);
		map.put("userB", userB);
		
		model.addAttribute("user", userA);
		model.addAttribute("users", list);
		model.addAttribute("userMap", map);
		return "basic/variable";
	}
	
	// TODO: 타임리프에서 사용할 수 있는 기본 객체
	// request는 유저가 들어왔다가 나가면 수명이 끝나지만 session은 계속 남아있다.(일정 시간 동안)
	@GetMapping("/basic-objects")
	public String basicObjects(HttpSession session) {
		// 물론 HttpServletRequest 해서 model에 담아서 넘겨도 되지만 타임리프가 제공해주니까 굳이 안써도 된다.
		session.setAttribute("sessionData", "Session");
		return "basic/basic-objects";
	}
	// param.파라미터 명 으로 파라미터로 넘긴 값을 model에 담아서 보내주지 않아도 그대로 쓸 수 있다!
	// session 도 $(session.세션 키) 로 쓸 수 있다.
	// spring bean도 만찬가지.
	
	// 빈으로 하나 등록 -> ${@helloBean.hello('...')} 로 사용 가능
	@Component("helloBean")
	static class HelloBean{
		public String hello(String data) {
			return "Hello " + data; 
		}
	}
	
	// TODO: 현재 시간 변경 처리 변수
	// 시간 변수 사용 (LocalDateTime 으로 현재시간을 타임리프에서 처리하는 방법)
	@GetMapping("/date")
	public String date(Model model) {
		model.addAttribute("localDateTime", LocalDateTime.now());
		return "basic/date";
	}
	
	// TODO: link 사용법
	// @{}을 쓰고, {param1} 으로 된 부분은 () 안에서 치환해주고 {}으로 없는 부분은 쿼리파라미터가 된다.
	@GetMapping("link")
	public ModelAndView link(ModelAndView mv) {
		mv.addObject("param1", "data1");
		mv.addObject("param2", "data2");
		mv.setViewName("basic/link");
		return mv;
	}
	
	// 리터럴
	// 타임리프에서 문자 리터럴은  '' 으로 감싸야 한다. but 공백 없이 이어지는 경우에는 생략이 가능
	// <span th:text="hello world!"></span> 은 공백이 있기 때문에 오류가 나온다
	// "'hello world!'" 로 수정해야 한다.
	// 그냥 리터럴 대체 문법인  '|...|' 을 사용하는게 좋다. (실무할 때도 '' + '' 이런식으로 쓰다가 그냥 이 문법으로 사용하게 됐음)
	@GetMapping("/literal")
	public String literal(Model model) {
		model.addAttribute("data", "Spring!");
		return "basic/literal";
	}
	
	// TODO: 연산으로 null 처리 방법
	// 연산 
	@GetMapping("/operation")
	public String operation(Model model) {
		model.addAttribute("nullData", null);
		model.addAttribute("data", "Spring");
		return "basic/operation";
	}
	// 비교 연산이나 이런건 if 문 처리를 보통 해와서...
	// Elvis 연산자만 알아둬도 유용하게 쓸 수 있겠다. (No operation 까지는 굳이?...)
	
	// 속성 값 설정
	// classappend 이외에 attappend, attrprepend 있지만 classappend를 쓰는게 좋아보인다.
	// checked는 true일 때만 check 되도록 사용해왔음
	@GetMapping("attribute")
	public String attribute() {
		return "basic/attribute";
	}
	
	// 반복
	// 타임리프에서 반복은 th:each 사용
	// th:each="user, stat : ${users}" 이렇게 많이 사용해왔음

	// 조건부 평가
	// lt나 ge 등을 이용해서 비교하기 보다는 명시적으로 <, > 을 사용하는게 좋아보인다.(문제 없음)
	// switch문만 살펴보기 -> status 값에 따른 태그 노출에 if문 대신 명시적으로 쓸 수 있을 것 같음
	// TODO: switch 문 이용하기
	@GetMapping("/condition")
	public String condition(Model model) {
		addUsers(model);
		return "basic/condition";
	}
	
	// 주석
	// 타임리프 파서 주석 : <!--/* [[${data}]] */--> 로 쓸 수 있음
	// 이렇게 하면 랜더링할 때 아예 보이지 않게됨
	
	// 블록
	// <th:block>
	// 보통 반복의 단위를 태그로 하기 어려울 때 사용 (반복되는 스코프? 개념으로 사용해왔음)
	
	// 자바스크립트 인라인
	// <script th:inline="javascript">
	// script 안에서 타임리프 변수 사용가능하게 해줌
	// TODO: javascript 안에서 타임리프 반복
	@GetMapping("/javascript")
	public String javascript(Model model) {
		model.addAttribute("user", new User("UserA", 10));
		addUsers(model);
		
		return "basic/javascript";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	// user 추가 메서드
	private void addUsers(Model model) {
		List<User> list = new ArrayList<>();
		list.add(new User("UserA", 10));
		list.add(new User("UserB", 20));
		list.add(new User("UserC", 30));
		
		model.addAttribute("users", list);
	}
	
	
	// 해당 클래스에서 쓸 내부 클래스(User 객체)
	@Data
	static class User{
		private String username;
		private int age;
		
		public User(String username, int age) {
			this.username = username;
			this.age = age;
		}
	}
	
}
