package jp.co.internous.team2409.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.team2409.model.domain.TblCart;
import jp.co.internous.team2409.model.domain.dto.CartDto;
import jp.co.internous.team2409.model.form.CartForm;
import jp.co.internous.team2409.model.mapper.TblCartMapper;
import jp.co.internous.team2409.model.session.LoginSession;

/**
 * カート情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/team2409/cart")
public class CartController {
	
	/*
	 * フィールド定義
	 */
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();
	
	/**
	 * カート画面を初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		int userId;
		if(loginSession.isLogined()) {
			userId = loginSession.getUserId(); 
		} else {
			userId = loginSession.getTmpUserId();
		}
		
		List<CartDto> carts = cartMapper.findByUserId(userId);
		carts.forEach(cart -> cart.setSubtotal(cart.getProductCount() * cart.getPrice()));
		m.addAttribute("carts", carts);
		
		m.addAttribute("loginSession", loginSession);
		
		return "cart";
	}
	
	/**
	 * カートに追加処理を行う
	 * @param f カート情報のForm
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/add")
	public String addCart(CartForm f, Model m) {
		
		int userId;
		if(loginSession.isLogined()) {
			userId = loginSession.getUserId(); 
		} else {
			userId = loginSession.getTmpUserId();
		}
		int productId = f.getProductId();
		
		TblCart cart = new TblCart();
		cart.setUserId(userId);
		cart.setProductId(productId);
		cart.setProductCount(f.getProductCount());
		
		int count = cartMapper.findCountByUserIdAndProuductId(userId, productId);
		if(count == 1) {
			cartMapper.update(cart);
		} else {
			cartMapper.insert(cart);
		}
		
		m.addAttribute("loginSession", loginSession);
		
		return "forward:/team2409/cart/";
	}

	/**
	 * カート情報を削除する
	 * @param checkedIdList 選択したカート情報のIDリスト
	 * @return true:削除成功、false:削除失敗
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteCart(@RequestBody String checkedIdList) {
		
		Map<String, List<Integer>> map = gson.fromJson(checkedIdList, Map.class);
		List<Integer> checkedIds = map.get("checkedIdList");
		cartMapper.deleteById(checkedIds);
		
		return true;
	}
}
