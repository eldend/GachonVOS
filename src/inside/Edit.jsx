import styles from "./inside.module.css"
import React, {useEffect, useState, useRef} from 'react';
import AdminHeader from "../main/AdminHeader";
import Footer from '../main/Footer';
import {useNavigate, useParams} from 'react-router-dom';
import axios from "axios";
import close from "../img/close.png";
import testuser from "../img/testuser.png";

export default function Edit() {

    const [modalIsOpen, setModalIsOpen] = useState(false);
    const {postId } = useParams();
    const [post, setPost] = useState({});
    const [comment, setComment] = useState('');
    const [loading, setLoading] = useState(true);
    const [filteredUserList, setFilteredUserList] = useState([]); 
    const [error, setError] = useState(null);
    const [selectedUser, setSelectedUser] = useState(null);
    const [processState, setProcessState] = useState('처리대기');
    const [dept, setDept] = useState('');
    const [tel, setTel] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
    const modalBackground = useRef();

    const navigate = useNavigate();

    const handleProcessState = (e) => {
        setProcessState(e.target.value);
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
                setLoading(false);
            } catch (error) {
                console.error('포스트 데이터를 불러오는 중 오류 발생:', error);
                setError('데이터를 가져오는 중 에러가 발생했습니다.');
            }
        };

        // const fetchCommentData = async () => {
        //     try {
        //         const commentApiUrl = `http://localhost:8080/api/post/${postId}/comment`;
        //         const response = await axios.get(commentApiUrl);
        //         const commentData = response.data;
        //         setComment(commentData);
        //         setLoading(false);
        //     } catch (error) {
        //         console.error('답글 데이터를 불러오는 중 오류 발생:', error);
        //         setError('데이터를 가져오는 중 에러가 발생했습니다.');
        //         setLoading(false);
        //     }
        // };
        //
        // fetchCommentData();
        fetchPostData();
    }, [postId]);
    const handlePostSubmission = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.put(`http://localhost:8080/api/post/${postId}/comment`, {
                    commentUpdateDto: {
                        comment,
                        dept,
                        tel
                    },
                    postsUpdateDto: {
                        processState
                    }
                },
                { withCredentials: true }
            );

            if (!response.data || response.status !== 200) {
                throw new Error('건의하기 서버 응답 오류');
            }

            console.log(response.data.message); // 개발 중 디버깅용 로깅, 프로덕션에서는 적절한 처리로 변경 필요
            // 페이지 이동
            navigate('/postmanage'); // '/your-pathname'에 실제 경로를 입력하세요
        } catch (error) {
            console.error('건의하기 에러:', error);
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
                <div className={styles.insideDetailWrap}>
                    <div className={styles.insideDetail}>
                        {post.content}
                    </div>
                    <div className={styles.insideAdd}>
                        {post.createdDate}
                    </div>
                </div>
                <div className={styles.insideManagerWrap}>
                    <div className={styles.insideManager}>
                        건의 처리 상태
                    </div>
                    <div className={styles.managerBoxWrap}>
                        <select id='process' value={processState} onChange={handleProcessState}>
                            <option value="처리대기">처리대기</option>
                            <option value="처리중">처리중</option>
                            <option value="처리완료">처리완료</option>
                        </select>
                    </div>
                </div>
                <div className={styles.managerInfo}>
                    <div className={styles.managerPartWrap}>
                        <div className={styles.managerPart}>
                            담당 부서
                        </div>
                        <input className={styles.managerTeam} placeholder='담당 부서(담당자명)'
                               id='dept' value={dept} onChange={(e) => setDept(e.target.value)}/>
                    </div>
                    <div className={styles.managerPartWrap}>
                        <div className={styles.managerPart}>
                            연락처
                        </div>
                        <input className={styles.managerTeam} placeholder='담당 부서 연락처 기재'
                               id='tel' value={tel} onChange={(e) => setTel(e.target.value)}/>
                    </div>
                </div>
                <div className={styles.managerCommentWrap}>
                    <input className={styles.managerComment} placeholder="해당 건의 사항에 대한 코멘트를 해주세요."
                           id='comment' value={comment} onChange={(e) => setComment(e.target.value)}/>
                </div>
                <div className={styles.manageBtnWrap}>
                    <button type= 'submit' className={styles.bottomChangeBtn} onClick={handlePostSubmission}>
                        수정하기
                    </button>
                </div>
            </div><Footer/></div>
    );
}