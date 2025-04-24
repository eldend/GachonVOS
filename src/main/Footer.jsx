import gachon from "../img/gachon.png";
import './footer.css';

const Footer = () => {
    return (
        <div className="footer">
        <div className="footerWrap">
            
            <img id="gcimg" src={gachon} width="240px" height="64px" />
            <div className="footerContentWrap">
                <div className="footerContent">
                    글로벌캠퍼스 : (13120) 경기도 성남시 수정구 성남대로 1342 TEL.031-750-5114
                </div>
                <div className="footerContent">
                    © 2021 Gachon University. All Rights Reserved
                </div>
            </div>
        </div>
        </div>
    );
};

export default Footer;