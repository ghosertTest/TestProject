var LastClick=-1;
var LastImg="";
var LastTD="";
var L_RightImgOpen=new Image();
var L_RightImgOver=new Image();
var L_TitleBG_Open=new Image();

if(self!=top){top.location=self.location;} //�Լ��ӵķ����

L_RightImgOpen="/images/LeftMenu_Right_Open.gif";

L_RightImgOver="/images/LeftMenu_Right_Over.gif";

function LeftMenuClick(ImgName)
{
	var objTR=event.srcElement.parentElement;
	var objTD=event.srcElement;
	var SelectIndex = objTR.rowIndex;
	var objStyle=objTR.parentElement.rows[SelectIndex+1].style;

	if (objStyle.display=="none")
	{
		if(LastClick!=-1){			
			LeftMenuClose(LastTD,LastClick,LastImg)
		} //�ر�֮ǰ�Ĳ˵�
		LeftMenuOpen(objTD,SelectIndex,ImgName)		
	}else{
		LeftMenuClose(objTD,SelectIndex,ImgName)
	}
	LastClick=SelectIndex;
	LastImg=ImgName;
	LastTD=objTD; //��¼���ڴ򿪵Ĳ˵�������Ϊ�´ιرոò˵����ݲ���	
}

function LeftMenuOpen(objName,CurrIndex,RightImg)
{
	RightImg.src=L_RightImgOpen;
	RightImg.style.display="";
	objName.style.backgroundImage="url(/images/LeftMenu_TitleBG_Open.gif)";
	objName.parentElement.parentElement.rows[CurrIndex+1].style.display = "";
}

function LeftMenuClose(objName,CurrIndex,RightImg)
{
	RightImg.src=L_RightImgOver;
	RightImg.style.display="none";
	objName.style.backgroundImage="url(/images/LeftMenu_TitleBG_Sub.gif)";
	objName.parentElement.parentElement.rows[CurrIndex+1].style.display = "none";
}

//��ʼ���˵�
function LeftMenuInit(CurrIndex)
{
	var ImgName=document.all("Img"+CurrIndex);
	var objTD=LeftTB.rows[CurrIndex].cells[0];
	
	ImgName.src=L_RightImgOpen;
	ImgName.style.display="";
	objTD.style.backgroundImage="url(/images/LeftMenu_TitleBG_Open.gif)";
	LeftTB.rows[CurrIndex+1].style.display="";
	LastClick=CurrIndex;
	LastTD=objTD;
	LastImg=ImgName;
}

function LeftMenuOver(RightImg)
{
	var objTR=event.srcElement.parentElement;
	var SelectIndex = objTR.rowIndex;
	var objStyle=objTR.parentElement.parentElement.rows[SelectIndex+1].style;

	if ((objStyle.display=="block")||(objStyle.display=="")){}
	else
		{RightImg.style.display="";}
}
function LeftMenuOut(RightImg)
{
	var objTR=event.srcElement.parentElement;
	var SelectIndex = objTR.rowIndex;
	var objStyle=objTR.parentElement.rows[SelectIndex+1].style;
	
	if ((objStyle.display=="block")||(objStyle.display=="")){}
	else
		{RightImg.style.display="none";}
}