import {Link, NavLink, useNavigate, useParams} from 'react-router-dom';
import "./admininside.css"
import React, {useEffect, useState, useRef} from 'react';
import chat from '../img/chat.png';
import testuser from "../img/testuser.png";
import AdminHeader from "../main/AdminHeader";
import Footer from '../main/Footer';
import axios from "axios";
import close from "../img/close.png";

export default function AdminInside() {

    const [modalIsOpen, setModalIsOpen] = useState(false);
    const { postId } = useParams();
    const [post, setPost] = useState({});
    const [loading, setLoading] = useState(true);
    const [filteredUserList, setFilteredUserList] = useState([]); 
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [selectedUser, setSelectedUser] = useState(null);
    const [modalOpen, setModalOpen] = useState(false);

    const modalBackground = useRef();

    const handleEdit = (postId) => {

        navigate(`/edit/${postId}`);
        console.log(`Edit post with id ${postId}`);
    };

    const handleDelete = async (postId) => {
        try {
            const response = await axios.delete(`http://localhost:8080/api/post/${postId}`);

            if (response.status === 200) {
                console.log(`Post with id ${postId} deleted successfully`);
                navigate("/postmanage")
            } else {
                console.error(`Failed to delete post with id ${postId}`);
            }
        } catch (error) {
            console.error("An error occurred while deleting the post", error);
        }
    };
            // 사용자 삭제 함수
            const handleDeleteUser = async () => {
                if (!selectedUser) {
                    return;
                }
            
                try {
                    // API 호출하여 사용자 삭제
                    const response = await fetch(`/api/user/${selectedUser.id}`, {
                        method: 'DELETE',
                    });
            
                    if (response.ok) {
                        // 삭제 성공 시 모달 닫기 및 사용자 목록 갱신
                        setModalOpen(false);
                        
                        // 페이지 새로고침
                        window.location.reload();
                    } else {
                        console.error('사용자 삭제에 실패했습니다.');
                    }
                } catch (error) {
                    console.error('사용자 삭제 중 오류 발생:', error);
                }
            };

            useEffect(() => {
                const fetchPostData = async () => {
                  try {
                    const postApiUrl = `http://localhost:8080/api/post/${postId}`;
                    const response = await axios.get(postApiUrl);
                    const postData = response.data;
                    setPost(postData);
            
                    // 작성자 정보를 가져오는 API 호출
                    const writerApiUrl = `http://localhost:8080/api/user/${postData.userId}`;
                    const writerResponse = await axios.get(writerApiUrl);
                    const writerData = writerResponse.data;
                    setFilteredUserList([writerData]); // 작성자 정보를 filteredUserList에 설정
            
                    setLoading(false);
                  } catch (error) {
                    console.error('포스트 데이터를 불러오는 중 오류 발생:', error);
                    setError('데이터를 가져오는 중 에러가 발생했습니다.');
                  }
                };
            
                fetchPostData();
              }, [postId]);
    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div>에러: {error}</div>;
    }
    console.log(post);
    console.log(loading);
    console.log(error);

    return (
        <div><AdminHeader/>
            <div className='insidePage'>
                <div className='insideTitleWrap'>
                    <div className='insideTitle'>
                        {post.title}
                    </div>
                    {filteredUserList.map((user, index) => ( 
                    <div key={index}>
                        <a onClick={() => {
                                        setSelectedUser(user);
                                        setModalOpen(true);
                                    }}>
                        <button className='whoisWriter'>
                            작성자 정보 조회
                        </button>
                    </a>
                    {modalOpen && selectedUser && (
                                        <div className={'managemodal-container'} ref={modalBackground} onClick={e => {
                                            if (e.target === modalBackground.current) {
                                                setModalOpen(false);
                                            }
                                        }}>
                                            <div className={'managemodal-content'}>
                                                <button className={'managemodal-close-btn'} onClick={() => setModalOpen(false)}>
                                                    <img src={close} width='15px' height='15px'/>
                                                </button>
                                                <div className='popupName'>사용자 정보</div>
                                                {/* 클릭한 사용자 정보를 이용하여 동적으로 표시 */}
                                                <img src={testuser} width='70px' height='70px' alt={selectedUser.username} />
                                                <div className='testUserName'>{selectedUser.username}</div>
                                                <div className='infoTable'>
                                                    <a className='info'>
                                                        <div className="infoName">아이디</div>
                                                        <div className="infoInfo">{selectedUser.username}</div>
                                                    </a>
                                                    <a className='info'>
                                                        <div className="infoName">학번</div>
                                                        <div className="infoInfo">{selectedUser.hakbun}</div>
                                                    </a>
                                                    <a className='info'>
                                                        <div className="infoName">이메일</div>
                                                        <div className="infoInfo">{selectedUser.email}</div>
                                                    </a>
                                                </div>
                                                <button className="deleteBtn2" onClick={handleDeleteUser}>
                                                    삭제
                                                </button>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            ))}
                            
                </div>
                <div className='insideDetailWrap'>
                    <div className='insideDetail'>
                        {post.content}
                    </div>
                    <div className='insideAdd'>
                        {post.createdDate}
                    </div>
                </div>
                <div className='insideManagerWrap'>
                    <div className='insideManager'>
                        건의 처리 상태
                    </div>
                    <div className='managerBoxWrap'>
                        <div className='managerBox'>
                            {post.processstate}
                        </div>
                    </div>
                </div>
                <div className='managerDetailWrap'>
                    <div className='managerInfo'>
                        <div className="managerPartWrap">
                            <div className="managerPart">담당 부서 :</div>
                            {/*<div className="managerTeam">{post.comments[0].dept}</div>*/}
                            {post.comments.map((comment, index) => (
                                <div className="managerTeam" key={index}>
                                    {comment.dept}
                                </div>
                            ))}
                        </div>
                        <div className="managerPartWrap">
                            <div className="managerPart">연락처 :</div>
                            {/*<div className="managerTeam">{post.comments[0].tel}</div>*/}
                            {post.comments.map((comment, index) => (
                                <div className="managerTeam" key={index}>
                                    {comment.tel}
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="managerComment">
                        {/* post.comments가 배열이라면 map 함수를 사용하여 각 아이템을 렌더링할 수 있습니다. */}
                        {post.comments.map((comment) => (
                            <div key={comment.id}>{comment.comment}</div>
                        ))}
                    </div>
                </div>
                <div className='managerDateWrap'>
                    <div className="managerDate">처리일자 : 
                    {post.comments.map((comment) => (
                        <span key={comment.id}>{comment.modifiedDate} </span>
                    ))}
                    </div>
                </div>

                <div className='muhanWrap'>
                    <div className='manageBtnWrap'>
                        <button className='bottomChangeBtn' onClick={() => handleEdit(post.id)}>
                        수정하기
                        </button>
                        <button className='bottomDeleteBtn' onClick={() => handleDelete(post.id)}>
                            삭제하기
                        </button>
                    </div>
                </div>
            </div><Footer/></div>
    );
}