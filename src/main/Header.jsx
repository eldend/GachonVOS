import React, { useState, useEffect } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import logo from "../img/vos.png";
import './Header.css';
import axios from 'axios';

const Header = () => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const [username, setUsername] = useState('');

  const activeStyle = {
    color: '#004E96',
    fontWeight: 700,
    borderBottom: '5px solid #004E96'
  };
  const checkUserLoggedIn = async () => {
    try {
      // 서버에 로그인 상태를 확인하는 요청을 보냅니다.
      const response = await axios.get('http://localhost:8080/api/user/login-status', { withCredentials: true });

      // 서버에서 반환한 로그인 상태를 기반으로 판단
      return response.data;
    } catch (error) {
      console.error('로그인 상태를 확인하는 중 오류 발생:', error);
      return false;
    }
  };
  useEffect(() => {
    const fetchData = async () => {
      const userIsLoggedIn = await checkUserLoggedIn();
      setLoggedIn(userIsLoggedIn);

      if (userIsLoggedIn) {
        await fetchUsername();
      }
    };
    fetchData();
  }, []);

  const fetchUsername = async () => {
    const usernameUrl = 'http://localhost:8080/api/user/username';
    try {
      const response = await axios.get(usernameUrl, { withCredentials: true });
      const username = response.data.username;
      const userrole = response.data.role;
      setUsername(username);
      setUserRole(userrole);
    } catch (error) {
      console.error('사용자 이름을 가져오는 중 오류 발생:', error);
    }
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
      <header>
        <div className="header-container">
          <Link to="/">
            <img src={logo} width="420px" height="120px" alt="VOS 로고" />
          </Link>
          <div className="right-links">
            <NavLink style={({ isActive }) => (isActive ? activeStyle : {})} to="/intro">VOS 소개</NavLink>
            <NavLink style={({ isActive }) => (isActive ? activeStyle : {})} to="/info">민원처리부서</NavLink>
            <NavLink style={({ isActive }) => (isActive ? activeStyle : {})} to="/vos">VOS</NavLink>
          </div>
        </div>
        <div className='button'>
          {loggedIn ? (
              <>
                <button className="loginBtn">
                  {userRole === 'USER' ? (
                      <Link to="/mypage">{username}</Link>
                  ) : userRole === 'ADMIN' ? (
                      <Link to="/postmanage">관리자페이지</Link>
                  ) : null}
                </button>
                <button className="loginBtn" onClick={handleLogout}>
                  로그아웃
                </button>
              </>
          ) : (
              <>
                <button className='loginBtn'>
                  <Link to="/userlogin" className='btnLink'>
                    로그인
                  </Link>
                </button>
                <button className='signupBtn'>
                  <Link to="/signup" className='btnLink'>
                    회원가입
                  </Link>
                </button>
              </>
          )}
        </div>
      </header>
  );
};
export default Header;