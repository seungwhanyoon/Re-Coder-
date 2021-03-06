package com.recoder.fatda.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.recoder.fatda.member.service.MemberService;
import com.recoder.fatda.member.vo.MemberVo;
import com.recoder.fatda.memberInfo.service.MemberInfoService;
import com.recoder.fatda.memberInfo.vo.MemberInfoVo;
import com.recoder.fatda.util.Paging;

/**
 * @author TJ
 *
 */
@Controller
public class MemberController {

	private final Logger log = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberInfoService memberInfoService;

	@RequestMapping(value = "/member/membercheck.do", method = RequestMethod.POST)
	public String memberCheck(MemberVo memberVo1, Model model) {
		log.debug("Welcome membermembercheck enter! - {}", memberVo1);

		MemberVo memberVo = memberService.memberExistCheck(memberVo1);

		model.addAttribute("memberVo1", memberVo1);
		model.addAttribute("memberVo", memberVo);

		String viewUrl;
		if (memberVo == null) {
			viewUrl = "member/memberchecksuccess";
		} else {

			viewUrl = "member/membercheckfail";
		}

		return viewUrl;
	}

//	@ResponseBody
//	@RequestMapping(value = "/member/membercheck.do")
//	public int memberCheck(MemberVo memberVo1, Model model) {
//		log.debug("Welcome membermembercheck enter! - {}", memberVo1);
//
//		MemberVo memberVo = memberService.memberExistCheck(memberVo1);
//		
//		model.addAttribute("memberVo1", memberVo1);
//		model.addAttribute("memberVo", memberVo);
//		
//		int memberchk= 0;
//		if(memberVo == null) {
//			
//		}else {
//			memberchk = 1;
//		}
//		
//		
//
//		return memberchk;
//	}

	// 조회
	@RequestMapping(value = "/member/list.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String memberList(@RequestParam(defaultValue = "1") int curPage,
			@RequestParam(defaultValue = "") String searchOption, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "noAscending") String order, Model model) {

		log.debug("Welcome MemberController memberList for Admisistrator !");

		int totalCount = memberService.memberCountTotal(searchOption, keyword);

		Paging memberPaging = new Paging(totalCount, curPage);

		int start = memberPaging.getPageBegin();
		int end = memberPaging.getPageEnd();

		List<MemberVo> memberList = memberService.memberSelectList(searchOption, keyword, start, end, order);

		Map<String, Object> pagingMap = new HashMap<>();
		pagingMap.put("totalCount", totalCount);
		pagingMap.put("memberPaging", memberPaging);

		model.addAttribute("memberList", memberList);
		model.addAttribute("paging", pagingMap);
		model.addAttribute("keyword", keyword);
		model.addAttribute("searchOption", searchOption);

		return "admin/memberListView";
	}

	// 1명 조회
	@RequestMapping(value = "/member/listOne.do")
	public String memberListOne(int no, Model model) {
		log.debug("Welcome memberListOne enter! - {}", no);

		MemberVo memberVo = memberService.memberSelectOne(no);

		model.addAttribute("memberVo", memberVo);

		return "member/memberListOneView";
	}

	@RequestMapping(value = "/common/index.do", method = RequestMethod.GET)
	public String index(Model model) {
		log.debug("Welcome IndexController 페이지 이동! ");

		return "common/index";
	}

	// 로그인페이지로 이동
	@RequestMapping(value = "/auth/login.do", method = RequestMethod.GET)
	public String login(Model model) {
		log.debug("Welcome MemberController login 페이지 이동! ");

		return "/auth/loginform";
	}

	// 로그인
	@RequestMapping(value = "/auth/login.do", method = RequestMethod.POST)
	public String longinCtr(MemberInfoVo memberInfoVo1, MemberVo memberVo1, HttpSession session, Model model) {

//		log.debug("Welcome MemberController loginCtr! " + memberVo1.getMemberEmail() + ", " + memberVo1.getMemberPassword());

		MemberVo memberVo = memberService.memberExist(memberVo1);
//		MemberInfoVo memberInfoVo = memberInfoService.memberInfoSelectOne(memberVo.getMemberNo());

		String viewUrl = "";
		if (memberVo != null && memberVo.getMemberAuth() == 'U') {
			MemberInfoVo memberInfoVo = memberInfoService.memberInfoSelectOne(memberVo.getMemberNo());

			// 회원이 존재한다면 세션에 담고
			// 회원 전체 조회 페이지로 이동
			session.setAttribute("login_memberVo", memberVo);
			session.setAttribute("_memberInfoVo", memberInfoVo);

			viewUrl = "redirect:/common/index.do";
		} else if (memberVo != null && memberVo.getMemberAuth() == 'A') {
			MemberInfoVo memberInfoVo = memberInfoService.memberInfoSelectOne(memberVo.getMemberNo());

			// 회원이 존재한다면 세션에 담고 // 회원 전체 조회 페이지로 이동
			session.setAttribute("login_memberVo", memberVo);
			session.setAttribute("_memberInfoVo", memberInfoVo);

			viewUrl = "redirect:/diet/list.do";
		} else {
			viewUrl = "/auth/loginfail";
		}

		return viewUrl;
	}

	// 로그아웃
	@RequestMapping(value = "auth/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session, Model model) {
		log.debug("Welcome MemberController logout 페이지 이동! ");

		// 세션의 객체들 파기
		session.invalidate();

		return "redirect:/common/index.do";
	}

	// 회원가입 페이지로
	@RequestMapping(value = "/member/add.do", method = RequestMethod.GET)
	public String memberAdd(@RequestParam(defaultValue = "") String memberEmail, Model model) {
		log.debug("Welcome MemberController memberAdd 페이지 이동! ");

		model.addAttribute("memberEmail", memberEmail);

		return "member/regiform";
	}

	// 회원가입
	@RequestMapping(value = "/member/add.do", method = RequestMethod.POST)
	public String memberAdd(String memberPasswordConfirm, MemberVo memberVo, Model model) {
		log.trace("Welcome MemberController memberAdd 신규등록 처리! " + memberVo);

		if (memberVo.getMemberPassword().equals(memberPasswordConfirm)) {

			try {
				memberService.memberInsertOne(memberVo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "/member/regifail";
			}

		} else {
			// 회원가입 실패시 처리할 페이지 추가하기
			return "/member/regifail";
		}

		return "redirect:/common/index.do";
	}

	// 마이페이지
	@RequestMapping(value = "/member/info.do", method = RequestMethod.GET)
	public String memberSelectOne(Model model) {
		log.debug("Welcome infoPage enter!");

		return "member/info";
	}

	// 회원정보 수정 페이지
	@RequestMapping(value = "/member/info.do", method = RequestMethod.POST)
	public String memberUpdate(@RequestParam(value = "memberNo") int no, Model model) {
		log.debug("Welcome memberUpdate enter! - {}", no);

		MemberVo memberVo = memberService.memberSelectOne(no);

		model.addAttribute("memberVo", memberVo);

		return "redirect:/member/update.do";
	}

	// 수정페이지
	@RequestMapping(value = "/member/update.do", method = RequestMethod.GET)
	public String memberUpdate(Model model) {
		log.debug("Welcome infoPage enter!");

		return "member/infoupdate";
	}

	@RequestMapping(value = "/member/updateCtr.do", method = RequestMethod.POST)
	public String memberUpdateCtr(HttpSession session, MemberVo memberVo, Model model) {
		log.debug("Welcome MemberController memberUpdateCtr {} :: {}", memberVo);

//		int resultNum = 0;

		try {
//			resultNum = memberService.memberUpdateOne(memberVo);
			memberService.memberUpdateOne(memberVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.setAttribute("login_memberVo", memberVo);

//		MemberVo sessionMemberVo = (MemberVo) session.getAttribute("login_memberVo");
//		if (resultNum != 0 && sessionMemberVo != null && sessionMemberVo.getMemberNo() == memberVo.getMemberNo()) {
//			session.setAttribute("login_memberVo", memberVo);
//		}

//		MemberVo sessionMemberVo = (MemberVo) session.getAttribute("login_memberVo");
//		// 데이터베이스에서 회원정보가 수정이 됬는지 여부
//		if (resultNum > 0 && sessionMemberVo != null && sessionMemberVo.getMemberNo() == memberVo.getMemberNo()) {
////				  MemberVo newMemberVo = new MemberVo();
//
//		  
//		  newMemberVo.setMemberNo(memberVo.getMemberNo());
//		  newMemberVo.setMemberEmail(memberVo.getMemberEmail());
//		  newMemberVo.setMemberName(memberVo.getMemberName());
//		  
//		  session.removeAttribute("login_memberVo");
//		  
//		  session.setAttribute("login_memberVo", newMemberVo); } 
//		}

		return "redirect:/member/info.do";
	}

	@RequestMapping(value = "/member/deleteCtr.do", method = RequestMethod.GET)
	public String memberDelete(int memberNo, Model model) {
		log.debug("Welcome MemberController memberDelete" + " 회원삭제 처리! - {}", memberNo);

		try {
			memberService.memberDeleteOne(memberNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 실패시 처리 페이지로 이동
		}

		// 메인페이지
		return "redirect:/member/list.do";
	}

	/*
	 * 관리자 컨트롤러
	 * *****************************************************************************
	 * ***
	 */

	// 로그인페이지로 이동

	/*
	 * @RequestMapping(value = "/admin/login.do", method = RequestMethod.GET) public
	 * String adminLogin(Model model) {
	 * log.debug("Welcome MemberController adminLogin 페이지 이동! ");
	 * 
	 * return "/admin/adminloginform"; }
	 */

	/*
	 * // 로그인
	 * 
	 * @RequestMapping(value = "/admin/login.do", method = RequestMethod.POST)
	 * public String adminLogin(MemberVo memberVo1, HttpSession session, Model
	 * model) {
	 * 
	 * log.debug("Welcome MemberController adminLogin! " +
	 * memberVo1.getMemberEmail() + ", " + memberVo1.getMemberPassword());
	 * 
	 * MemberVo memberVo = memberService.memberExist(memberVo1);
	 * 
	 * String viewUrl = ""; if (memberVo != null && memberVo.getMemberAuth() == 'A')
	 * {
	 * 
	 * // 회원이 존재한다면 세션에 담고 // 회원 전체 조회 페이지로 이동
	 * session.setAttribute("login_memberVo", memberVo);
	 * 
	 * viewUrl = "redirect:/diet/list.do"; } else { viewUrl =
	 * "redirect:/admin/login.do"; }
	 * 
	 * return viewUrl; }
	 */

	// 로그아웃

	/*
	 * @RequestMapping(value = "/admin/logout.do", method = RequestMethod.GET)
	 * public String adminLogout(HttpSession session, Model model) {
	 * log.debug("Welcome MemberController logout 페이지 이동! ");
	 * 
	 * session.invalidate();
	 * 
	 * return "redirect:/admin/login.do"; }
	 */

}