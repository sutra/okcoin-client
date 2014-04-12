/**
 * 撤销委托
 * @param entrustId
 */
function cancelEntrust(entrustId,type){
	var symbol = document.getElementById("symbol").value;
	var url = "/trade/cancelEntrust.do?random="+Math.round(Math.random()*100);
	var param={entrustId:entrustId,symbol:symbol};
	if(document.getElementById("tradeType") != null){
		type = document.getElementById("tradeType").value;
	}
	jQuery.post(url,param,function(data){
		if(data == 0){
			document.getElementById("entrustStatus"+entrustId).innerHTML="已撤销";
			if(type == 3){
				var currentPage = document.getElementById("currentPage").value;
				var status =  document.getElementById("selectedStatus").value;
				window.location.href = "/trade/entrust.do?status="+status+"&currentPage="+currentPage;
			}else if(symbol == 0){
				window.location.href = "/trade/btc.do?tradeType="+type;
			}else{
				window.location.href = "/trade/ltc.do?tradeType="+type;
			}
		}
	});
}

function tradeTurnoverValue(){
	tradeTurnoverValue(0);
}
//type :0 默认 type=1 price type=2 amount
function tradeTurnoverValue(type){
	var tradeType = document.getElementById("tradeType").value;
	var symbol = document.getElementById("symbol").value;
	var tradeAmount = 0;
	var tradeCnyPrice = 0;
	if(type == 2){
		var  amountSelectionStart = getPositionForInput(document.getElementById("tradeAmount"));
		tradeAmount = document.getElementById("tradeAmount").value= checkNumberByName("tradeAmount");
		setCursorPosition(document.getElementById("tradeAmount"),amountSelectionStart);
	}else{
		tradeAmount = document.getElementById("tradeAmount").value;
	}
	if(type == 1){
		var  priceSelectionStart = getPositionForInput(document.getElementById("tradeCnyPrice"));
		tradeCnyPrice =document.getElementById("tradeCnyPrice").value =  checkNumberByName("tradeCnyPrice");
		setCursorPosition(document.getElementById("tradeCnyPrice"),priceSelectionStart);
	}else{
		tradeCnyPrice =document.getElementById("tradeCnyPrice").value;
	}
	var turnover = tradeAmount*tradeCnyPrice;
	if(turnover!= null && turnover.toString().split(".")!=null && turnover.toString().split(".")[1] != null && turnover.toString().split(".")[1].length>4){
		turnover=turnover.toFixed(4);		
	}
	document.getElementById("tradeTurnover").value = turnover;
	if(tradeType ==0){
		var tradeTurnover = tradeAmount*tradeCnyPrice;
		if(document.getElementById("userBalance")!=null && Number(document.getElementById("userBalance").value) < Number(tradeTurnover)){
			alertTipsSpan("您的余额不足，请先充值");
			return;
		}else{
			clearTipsSpan();
		}
	}else{
		if(document.getElementById("userBalance")!=null && Number(document.getElementById("userBalance").value ) < Number(tradeAmount)){
			if(symbol == 0){
				alertTipsSpan("您的BTC余额不足");
			}else{
				alertTipsSpan("您的LTC余额不足");
			}
			return;
		}else{
			clearTipsSpan();
		}
	}
}
var check = 1;
function submitTradeBtcForm(){
	if(check == 2){
		return;
	}
	var tradeAmount =document.getElementById("tradeAmount").value;
	var tradeCnyPrice =document.getElementById("tradeCnyPrice").value;
	var tradePwd = trim(document.getElementById("tradePwd").value);
	var tradeType = document.getElementById("tradeType").value;
	var symbol = document.getElementById("symbol").value;
	var isopen = document.getElementById("isopen").value;
	var islimited = document.getElementById("limitedType").checked;
	var limited = 0;
	if(!islimited){
		if(tradeType ==0){
			var tradeTurnover = tradeAmount*tradeCnyPrice;
			if(document.getElementById("userBalance")!=null &&  Number(document.getElementById("userBalance").value) <  Number(tradeTurnover)){
				alertTipsSpan("您的余额不足，请先充值");
				return;
			}else{
				clearTipsSpan();
			}
		}else{
			if(document.getElementById("userBalance")!=null &&  Number(document.getElementById("userBalance").value) <  Number(tradeAmount)){
				if(symbol == 0){
					alertTipsSpan("您的BTC余额不足");
				}else{
					alertTipsSpan("您的LTC余额不足");
				}
				return;
			}else{
				clearTipsSpan();
			}
		}
		 var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
		 if(!reg.test(tradeAmount) ){
			 alertTipsSpan("请输入交易数量");
			return;
		 }else{
				clearTipsSpan();
		}
		if(tradeAmount < 0.01){
			if(symbol==1) alertTipsSpan("最小交易数量为：0.1LTC！");
			 else alertTipsSpan("最小交易数量为：0.01BTC！");
			return;
		}else{
			clearTipsSpan();
		}
		 if(!reg.test(tradeCnyPrice) ){
			 alertTipsSpan("请输入价格");
			return;
		 }else{
				clearTipsSpan();
		}	
	}else{
		limited = 1;
		var limitedMoney = Number(document.getElementById("limitedMoney").value);
		if(tradeType == 0){
			var nowPrice = accMul(0.01,Number(document.getElementById("nowPrice").value));
			if(symbol == 1){
				nowPrice = accMul(0.1,Number(document.getElementById("nowPrice").value));
			}
			if(limitedMoney < nowPrice){
				if(symbol == 0){
					alertTipsSpan("最小购买0.01btc");
				}else{
					alertTipsSpan("最小购买0.1ltc");
				}
				return;
			}
		}else{
			if(symbol == 0){
				if(limitedMoney < 0.01){
					alertTipsSpan("最小卖出0.01btc");
					return;
				}
			}else{
				if(limitedMoney < 0.1){
					alertTipsSpan("最小卖出0.1ltc");
					return;
				}
			}
		}
		if(tradeType ==0){
			if(document.getElementById("userBalance")!=null &&  Number(document.getElementById("userBalance").value) <  limitedMoney){
				alertTipsSpan("您的余额不足，请先充值");
				return;
			}else{
				clearTipsSpan();
			}
			tradeCnyPrice = limitedMoney;
		}else{
			if(document.getElementById("userBalance")!=null &&  Number(document.getElementById("userBalance").value) <  limitedMoney){
				if(symbol == 0){
					alertTipsSpan("您的BTC余额不足");
				}else{
					alertTipsSpan("您的LTC余额不足");
				}
				return;
			}else{
				clearTipsSpan();
			}
		}
		tradeAmount = limitedMoney;
	}
	if(tradePwd == "" && isopen != 1){
		alertTipsSpan("请输入交易密码");
		return;
	}else{
		document.getElementById("tradeBtcTips").style.display="";
		document.getElementById("tradeBtcTips").innerHTML="&nbsp;";
	}
	var url = "";
	if(tradeType ==0){
		url = "/trade/buyBtcSubmit.do?random="+Math.round(Math.random()*100);
	}else{
		url = "/trade/sellBtcSubmit.do?random="+Math.round(Math.random()*100);
	}
	var param={tradeAmount:tradeAmount,tradeCnyPrice:tradeCnyPrice,tradePwd:tradePwd,symbol:symbol,limited:limited};
	check = 2;
	jQuery.post(url,param,function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			if(result.resultCode != 0){
				check = 1;
			}
			if(result.resultCode == -1){
				 if(tradeType ==0){
					 if(symbol==1) alertTipsSpan("最小购买数量为：0.1LTC！");
					 else alertTipsSpan("最小购买数量为：0.01BTC！");
				 }else{ 
					 if(symbol==1) alertTipsSpan("最小卖出数量为：0.1LTC！");
					 else alertTipsSpan("最小卖出数量为：0.01BTC！");
				 }
			 }else if(result.resultCode == -2){
				 if(result.errorNum == 0){
					 alertTipsSpan("交易密码错误五次，请2小时后再试！");
				 }else{
					 alertTipsSpan("交易密码不正确！您还有"+result.errorNum+"次机会");
				 }
				 if(document.getElementById("tradePwd") != null){
					 document.getElementById("tradePwd").value = "";
				 }
			 }else if(result.resultCode == -3){
				 alertTipsSpan("出价不能为0！");
			 }else if(result.resultCode == -4){
				 alertTipsSpan("余额不足！");
			 }else if(result.resultCode == -5){
				 alertTipsSpan("您未设置交易密码，请设置交易密码。");
			 }else if(result.resultCode == -6){
				 okcoinAlert("您输入的价格与最新成交价相差太大，请检查是否输错",null,null,"");
			 }else if(result.resultCode == -7){
				 alertTipsSpan("交易密码免输超时，请刷新页面输入交易密码后重新激活。");
			 }else if(result.resultCode == -8){
				 alertTipsSpan("请输入交易密码");
			 }else if(result.resultCode == 0){
				 if(symbol ==0){ 
					 window.location.href="/trade/btc.do?tradeType="+tradeType+"&success";
				 }else{
					 window.location.href="/trade/ltc.do?tradeType="+tradeType+"&success";
				 }
			 }else if(result.resultCode == 2){
				 alertTipsSpan("账户出现安全隐患已被冻结，请尽快联系客服。");
			 }
		}
	});	
}

function clearTipsSpan(){
	document.getElementById("tradeBtcTips").style.display="";
	document.getElementById("tradeBtcTips").innerHTML="";
}

function alertTipsSpan(tips){
	document.getElementById("tradeBtcTips").style.display="";
	document.getElementById("tradeBtcTips").innerHTML=tips;
}

function summoneyValue(){
	var tradeType = document.getElementById("tradeType").value;
	var symbol = document.getElementById("symbol").value;
	var  turnoverSelectionStart = getPositionForInput(document.getElementById("tradeTurnover"));
	var tradeTurnover = document.getElementById("tradeTurnover").value=checkNumberByName("tradeTurnover");
	setCursorPosition(document.getElementById("tradeTurnover"),turnoverSelectionStart);
	var tradeCnyPrice = document.getElementById("tradeCnyPrice").value;
	var tradeAmount =document.getElementById("tradeAmount").value;
	tradeAmount = tradeTurnover/tradeCnyPrice;
	if(tradeAmount!= null && tradeAmount.toString().split(".")!=null &&tradeAmount.toString().split(".")[1] != null && tradeAmount.toString().split(".")[1].length>4){
		tradeAmount=tradeAmount.toFixed(5);		
		tradeAmount = tradeAmount.substring(0, tradeAmount.length-1);
	}
	document.getElementById("tradeAmount").value=tradeAmount;
	var reg=/^(-?\d*)\.?\d{1,4}$/;
    if(tradeTurnover!=null && tradeTurnover.toString().split(".")!=null && tradeTurnover.toString().split(".")[1]!=null && tradeTurnover.toString().split(".")[1].length>4){
    	if(!reg.test(tradeTurnover)){
        	document.getElementById("tradeTurnover").value = tradeTurnover.substring(0, tradeTurnover.length-1);
            return false;
        }
    }
	if(tradeType ==0){
		if(document.getElementById("userBalance")!=null && Number(document.getElementById("userBalance").value) < Number(tradeTurnover)){
			alertTipsSpan("您的余额不足，请先充值");
			return;
		}else{
			clearTipsSpan();
		}
	}else{
		if(document.getElementById("userBalance")!=null && Number(document.getElementById("userBalance").value ) < Number(tradeAmount)){
			if(symbol == 0){
				alertTipsSpan("您的BTC余额不足");
			}else{
				alertTipsSpan("您的LTC余额不足");
			}
			return;
		}else{
			clearTipsSpan();
		}
	}
}

function limitedSummoneyValue(){
	var limitedMoney = document.getElementById("limitedMoney");
	var tradeType = document.getElementById("tradeType").value;
	var length = tradeType==0?2:4;
	var priceSelectionStart = getPositionForInput(limitedMoney);
	var money =limitedMoney.value=(function (a) {return a.length > 1 ? a.shift().replace(/\D/g, '') + '.' + a.join('').replace(/\D/g, '').slice(0, length) : a[0].replace(/\D/g,'');})(limitedMoney.value.split('.'));
	setCursorPosition(limitedMoney,priceSelectionStart);
	var symbol = document.getElementById("symbol").value;
	if(tradeType == 0){
		var userBalance = Number(document.getElementById("userCnyBalance").value);
		if(money > userBalance){
			alertTipsSpan("您的余额不足，请先充值 ");
			return;
		}
	}else{
		var coinBalance = Number(document.getElementById("userCoinBalance").value);
		if(money > coinBalance){
			if(symbol == 0){
				alertTipsSpan("您的BTC余额不足");
			}else{
				alertTipsSpan("您的LTC余额不足");
			}
			return;
		}
	}
	clearTipsSpan();
}

function getEntrust(){
	var status =  document.getElementById("selectedStatus").value;
	window.location.href = "/trade/entrust.do?status="+status;
}

function autoTrade(index,type,symbol){
	document.getElementById("limitedType").checked = "";
	limitedTypeChange();
	var priceNum = 0;
	var amountNum = 0;
	var tradeType = document.getElementById("tradeType").value;
	if(tradeType == type){
		type = type==0?1:0;
		if(type == 0){
			document.getElementById("buyDiv").style.display = "";
			document.getElementById("sellDiv").style.display = "none";
			document.getElementById("buyLi").className = "cur";
			document.getElementById("sellLi").className = "";
			document.getElementById("tradeType").value = 0;
			document.getElementById("priceSpan").innerHTML = "出价￥/BTC:";
			document.getElementById("numSpan").innerHTML = "<span class=\"lightgreen5 fontsize-16\">购买数量</span>";
			document.getElementById("summoneySpan").innerHTML = "总金额";
			document.getElementById("btnA").innerHTML = "立即买入";
			document.getElementById("btnA").className = "buttonGreen";
			document.getElementById("limitpriceTitle").innerHTML = "金额 : ";
			document.getElementById("userBalance").value = document.getElementById("userCnyBalance").value;
			document.getElementById("limitpriceTitle").style.color = "green";
			document.getElementById("proprotionInfo").innerHTML = "买入比例：";
			document.getElementById("proprotionInfo").style.color = "green";
			tradeType = 0;
		}else{
			document.getElementById("sellDiv").style.display = "";
			document.getElementById("buyDiv").style.display = "none";
			document.getElementById("sellLi").className = "cur";
			document.getElementById("buyLi").className = "";
			document.getElementById("tradeType").value = 1;
			document.getElementById("priceSpan").innerHTML = "售价￥/BTC:";
			document.getElementById("numSpan").innerHTML = "<span class=\"red fontsize-16\">卖出数量</span>";
			document.getElementById("summoneySpan").innerHTML = "兑换额CNY";
			document.getElementById("btnA").innerHTML = "立即售出";
			document.getElementById("btnA").className = "buttonRed";
			document.getElementById("limitpriceTitle").innerHTML = "数量 : "
			document.getElementById("userBalance").value = document.getElementById("userCoinBalance").value;
			document.getElementById("limitpriceTitle").style.color = "red";
			document.getElementById("proprotionInfo").innerHTML = "卖出比例：";
			document.getElementById("proprotionInfo").style.color = "red";
			tradeType = 1;
		}
	}
	var name = "sell";
	if(tradeType == 1){
		name = "buy";
	}
	var priceName = name+"Price"+index;
	priceNum =	Number(document.getElementById(priceName).value);
	for ( var i = 1; i <= index; i++) {
		var amountName = name+"Amount"+i;
		var amount = Number(document.getElementById(amountName).value);
		amountNum = accAdd(amountNum,amount);
	}
	var money = Number(document.getElementById("userBalance").value);
	var moneyNum = amountNum * priceNum;
	if(tradeType == 0){
		var reg=/^(-?\d*)\.?\d{1,2}$/;
		if(money!=null && money.toString().split(".")!=null && money.toString().split(".")[1]!=null && money.toString().split(".")[1].length>2){
			if(!reg.test(money)){
				var end =  money.toString().split(".")[1];
				if(end.length>2){
					end = end.substring(0, 2);
				}
				money = money.toString().split(".")[0]+"."+end;
			}
		}
		if(money < moneyNum){
			document.getElementById("tradeCnyPrice").value = priceNum;
			document.getElementById("tradeTurnover").value = money;
			summoneyValue();
		}else{
			document.getElementById("tradeCnyPrice").value = priceNum;
			document.getElementById("tradeAmount").value = amountNum;
			tradeTurnoverValue();
		}
	}else{
		var reg=/^(-?\d*)\.?\d{1,4}$/;
		if(money!=null && money.toString().split(".")!=null && money.toString().split(".")[1]!=null && money.toString().split(".")[1].length>4){
			if(!reg.test(money)){
				var end =  money.toString().split(".")[1];
				if(end.length>4){
					end = end.substring(0, 4);
				}
				money = money.toString().split(".")[0]+"."+end;
			}
		}
		if(money < amountNum){
			document.getElementById("tradeAmount").value = money;
			document.getElementById("tradeCnyPrice").value = priceNum;
			tradeTurnoverValue();
		}else{
			document.getElementById("tradeCnyPrice").value = priceNum;
			document.getElementById("tradeAmount").value = amountNum;
			tradeTurnoverValue();
		}
	}
}
function antoTurnover(money){
	var reg=/^(-?\d*)\.?\d{1,2}$/;
	if(money!=null && money.toString().split(".")!=null && money.toString().split(".")[1]!=null && money.toString().split(".")[1].length>2){
    	if(!reg.test(money)){
    		var end =  money.toString().split(".")[1];
    		if(end.length>2){
    			end = end.substring(0, 2);
    		}
    		money = money.toString().split(".")[0]+"."+end;
        }
    }
	document.getElementById("tradeTurnover").value = money;
	document.getElementById("limitedMoney").value = money;
	summoneyValue();
}
function antoAmount(money){
	var reg=/^(-?\d*)\.?\d{1,4}$/;
	if(money!=null && money.toString().split(".")!=null && money.toString().split(".")[1]!=null && money.toString().split(".")[1].length>4){
    	if(!reg.test(money)){
    		var end =  money.toString().split(".")[1];
    		if(end.length>4){
    			end = end.substring(0, 4);
    		}
    		money = money.toString().split(".")[0]+"."+end;
        }
    }
	document.getElementById("tradeAmount").value = money;
	document.getElementById("limitedMoney").value = money;
	tradeTurnoverValue();
}
function updateSecond(){
	var second = document.getElementById("updateSecond").value;
	var secondCookie = new CookieClass();
	secondCookie.setCookie("REFRESH_HANDLEENTRUST_TIME", second);
	if(entrustTime != null){
		clearTimeout(entrustTime);
	}
	if(updateTime != null){
		clearInterval(updateTime);
	}
	updateNumber = second/1000-1;
	updateTime = setInterval(updateNumberFun, 1000);
	entrustTime = setTimeout("handleEntrust("+second+")", second);
}
function autoSecond(time){
	var updateSecond = document.getElementById("updateSecond");
	for(var i=0;i<updateSecond.options.length;i++){
	if(updateSecond.options[i].value == time){
		updateSecond.options[i].selected = true;
		break;
		}
	}
	entrustTime = setTimeout("handleEntrust("+time+")", time);
	updateNumber = time/1000-1;
	if(updateTime != null){
		clearInterval(updateTime);
	}
	updateTime = setInterval(updateNumberFun, 1000);
}
function limitedTypeChange(){
	if(document.getElementById("limitedType").checked){
		document.getElementById("treadContTextDiv").style.display = "none";
		document.getElementById("treadContDiv").style.display = "none";
		document.getElementById("limitedDiv").style.display = "";
	}else{
		document.getElementById("treadContTextDiv").style.display = "";
		document.getElementById("treadContDiv").style.display = "";
		document.getElementById("limitedDiv").style.display = "none";
	}
}
function onPortion(portion){
	var tradeType = document.getElementById("tradeType").value;
	if(tradeType == 0){
		var money = Number(document.getElementById("userCnyBalance").value);
		var portionMoney = accMul(money,portion);
		antoTurnover(portionMoney);
	}else{
		var money = Number(document.getElementById("userCoinBalance").value);
		var portionMoney = accMul(money,portion);
		antoAmount(portionMoney);
	}
}