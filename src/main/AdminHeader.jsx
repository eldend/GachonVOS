import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import adminLogo from "../img/admin.png";
import "./adminHeader.css";
import axios from 'axios';

const AdminHeader = () => {
    const [loggedIn, setLoggedIn] = useState(false);
    const activeStyle = {
        color: '#8A8A8A',
        fontWeight: 700,
        borderBottom: '5px solid #004E96'
    };

    const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await axios.post('http://localhost:8080/logout', null, { withCredentials: true });

      // useNavigate를 사용하여 '/userlogin'으로 이동합니다.
      navigate('/userlogin');

      setLoggedIn(false);
    } catch (error) {
      console.error('로그아웃 중 오류 발생:', error);
    }
  };

    return (
        <div className='adminHeaderWrap'>
            <div className="adminHeader">
                <Link to="/postmanage">
                    <img src={adminLogo} width="420px" height="120px" />
                </Link>
                <div className='buttonHeader'>
                    <button className='adminBtn'>
                            관리자 님
                    </button>
                    <button className='logoutBtn' onClick={handleLogout}>
                        로그아웃
                    </button>
                </div>
            </div>
            <div className="manageTab">
                <NavLink style={({ isActive }) => (isActive ? activeStyle : {})} to="/usermanage">사용자 관리</NavLink>
                <NavLink style={({ isActive }) => (isActive ? activeStyle : {})} to="/postmanage">게시글 관리</NavLink>
            </div>
        </div>
    );
};

export default AdminHeader;
