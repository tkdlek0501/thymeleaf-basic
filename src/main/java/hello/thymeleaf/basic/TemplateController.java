package hello.thymeleaf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("template")
public class TemplateController {
	
	// TODO: *템플릿 레이아웃 (공통 html 레이아웃 처리 및 개별적인 추가)
	// 템플릿 레이아웃
	@GetMapping("layout")
	public String layout() {
		return "template/layout/layoutMain";
	}
	
	// html 일부 replace
	@GetMapping("/layoutExtend")
	public String layoutExtend() {
		return "template/layout/layoutExtendMain";
	}
	
	
}
