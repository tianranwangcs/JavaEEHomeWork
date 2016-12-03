package Test;
import Annotation.Controller;
import Annotation.RequestMapping;
import MVC.ModelAndView;
@Controller
public class Test {
	@RequestMapping("/hello")
	public ModelAndView  hello(ModelAndView inMav) {
		ModelAndView mav=inMav;
		mav.setViewName("test");
		//get params from submit button, set it in paramMap
		//then set it to objectMap
		mav.addObject("name", mav.getParam("name"));
		mav.addObject("pas", mav.getParam("pas"));
		return mav;
	}
}