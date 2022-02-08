var router;
router = new VueRouter({
    "mode": "history",
    "routes": [
        {
            "name": "Application",
            "path": "/",
            "redirect": "/main",
            "component": MainLayout,
            "children": [
                {
                    "name": "메인화면",
                    "path": "/main",
                    "component": MainPage
                },
                {
                    "name": "대시보드",
                    "path": "/dashboard",
                    "component": DashboardPage
                },
                {
                    "name": "강의관리",
                    "path": "/dashboard/lecture",
                    "redirect": "/dashboard/lecture/list",
                },
                {
                    "name": "강의목록",
                    "path": "/dashboard/lecture/list",
                    "component": DashboardLectureListMainPage,
                },
                {
                    "name": "강의상세",
                    "path": "/dashboard/lecture/list/detail",
                    "component": DashboardLectureListDetailPage,
                },
                {
                    "name": "수강 관리 메세지 보내기",
                    "path": "/dashboard/course/sendMessage",
                    "component": SendMessage
                },
                {
                    "name": "결제관리",
                    "path": "/dashboard/payment",
                    "component": DashboardPaymentMainPage,
                },
                {
                    "name": "매출내역",
                    "path": "/dashboard/payment/sales",
                    "component": DashboardPaymentSalesPage
                },
                {
                    "name": "정산내역",
                    "path": "/dashboard/payment/calculate",
                    "component": DashboardPaymentCalculatePage
                },
                {
                    "name": "결제내역",
                    "path": "/dashboard/payment/payment",
                    "component": DashboardPaymentPaymentPage
                },
                {
                    "name": "학생 리스트",
                    "path": "/dashboard/course/students",
                    "component": StudentList
                },
                {
                    "name": "학생 리스트 상세",
                    "path": "/dashboard/course/students/detail",
                    "component": StudentListDetail
                },
                {
                    "name": "학부모 리스트",
                    "path": "/dashboard/course/parents",
                    "component": ParentsList
                },
                {
                    "name": "학생 강의 목록",
                    "path": "/dashboard/classroom",
                    "component": DashboardClassroomMainPage,
                },
                {
                    "name": "결제취소내역",
                    "path": "/dashboard/payment/cancel",
                    "component": DashboardPaymentCancelPage
                },
                {
                    "name": "1:1채팅",
                    "path": "/mypage/personal-chat/main",
                    "component": MypagePersonalChatMainPage,
                    "props": true
                },
                {
                    "name": "마이페이지",
                    "path": "/mypage/lecturers/main",
                    "component": MypageLecturerMainPage
                },
                {
                    "name": "마이페이지 수정",
                    "path": "/mypage/lecturers/info",
                    "component": MypageLecturerInfoPage
                },
                {
                    "name": "강의실",
                    "path": "/classroom",
                    "redirect": "/classroom/main",
                },
                {
                    "name": "강의실 목록",
                    "path": "/classroom/main",
                    "component": ClassRoomMainPage
                },
                {
                    "name": "강의실 상세",
                    "path": "/classroom/detail",
                    "component": ClassRoomDetailPage
                },
                {
                    "name": "강의실 입장",
                    "path": "/main/lectures/enter",
                    "component": MainLecturesEnterMainPage
                },
                {
                    "name": "라이브미팅 생성",
                    "path": "/mypage/lecturers/live/edit",
                    "component": MypageLecturerLiveEditPage
                },
                {
                    "name": "라이브미팅 상세화면",
                    "path": "/mypage/lecturers/live/detail",
                    "component": MypageLecturerLiveDetailPage
                },
                {
                    "name": "문의하기",
                    "path": "/mypage/lecturers/contact-us",
                    "component": MypageLecturerContactUsPage
                },
                {
                    "name": "강의실 채팅",
                    "path": "/main/lectures/chat",
                    "component": MainLecturesClassRoomChatPage
                },
                {
                    "name": "계정찾기",
                    "path": "/find-account",
                    "component": FindAccountPage
                },
                {
                    "name": "사용자 마이페이지",
                    "path": "/mypage/users/main",
                    "component": MypageUsersMainPage
                },
                {
                    "name": "사용자 마이페이지 수정",
                    "path": "/mypage/users/info",
                    "component": MypageUsersInfoPage
                },
                {
                    "name": "사용자 문의하기",
                    "path": "/mypage/users/contact-us",
                    "component": MypageUserContactUsPage,
                    "props": true
                },
                {
                    "name": "사용자 결제페이지",
                    "path": "/mypage/users/payment",
                    "component": MypageUserPaymentPage,
                },
                {
                    "name": "강사 공지사항",
                    "path": "/mypage/lecturer/board",
                    "component": lecturerBoardPage
                },
                {
                    "name": "강사 공지사항 리스트",
                    "path": "/mypage/lecturer/board/list",
                    "component": lecturerBoardListPage
                },
                {
                    "name": "개인정보처리방침",
                    "path": "/privacy",
                    "component": PrivacyPage
                },
                {
                    "name": "이용약관 동의",
                    "path": "/policy",
                    "component": PolicyPage
                },
            ]
        },
        {
            "name": "ApplicationAdmin",
            "path": "/settings",
            "redirect": "/settings/apis",
            "component": AdminLayout,
            "children": [
                {
                    "name": "환경설정",
                    "path": "/settings",
                    "redirect": "/settings/apis",
                },
                {
                    "name": "API 관리",
                    "path": "/settings/apis",
                    "component": SettingApiMainPage
                },
                {
                    "name": "API 관리 상세",
                    "path": "/settings/apis/detail",
                    "component": SettingApiDetailMainPage
                },
                {
                    "name": "메뉴 관리",
                    "path": "/settings/menus",
                    "component": SettingMenuMainPage
                },
                {
                    "name": "역할 관리",
                    "path": "/settings/roles",
                    "component": SettingRoleMainPage
                },
                {
                    "name": "역할 관리 상세",
                    "path": "/settings/roles/detail",
                    "component": SettingRoleDetailMainPage
                },
                {
                    "name": "사용자 관리",
                    "path": "/settings/users",
                    "component": SettingUserMainPage
                },
                {
                    "name": "코드 관리",
                    "path": "/settings/codes",
                    "component": SettingCodeMainPage
                },
                {
                    "name": "약관 관리",
                    "path": "/settings/terms",
                    "component": SettingTermsMainPage
                },
                {
                    "name": "약관 관리 상세",
                    "path": "/settings/terms/detail",
                    "component": SettingTermsDetailMainPage
                },
                {
                    "name": "카테고리 관리",
                    "path": "/settings/categories",
                    "component": SettingCategoryMainPage
                },
                {
                    "name": "라이브스트리밍",
                    "path": "/settings/live-streaming",
                    "component": SettingLiveStreamingMainPage
                },
                {
                    "name": "카테고리별 라이브",
                    "path": "/settings/category-live",
                    "component": SettingCategoryLiveMainPage
                },
                {
                    "name": "데모영상관리",
                    "path": "/settings/demo",
                    "component": SettingDemoMainPage
                },

                {
                    "name": "공지사항 관리",
                    "path": "/settings/board",
                    "component": SettingBoardMainPage
                },
                {
                    "name": "공지사항 상세",
                    "path": "/settings/board/detail",
                    "component": SettingBoardDetailPage
                },
                {
                    "name": "강사 등록 승인",
                    "path": "/settings/lecturer",
                    "component": SettingLecturerMainPage
                },
                {
                    "name": "수강 관리",
                    "path": "/settings/course",
                    "component": CourseLayout,
                    "children": [
                        {
                            "name": "강사 별 학생 리스트",
                            "path": "/settings/lecturerCourse",
                            "component": SettingLecturerCourseMainPage,
                        },
                        {
                            "name": "강사 별 학부모 리스트",
                            "path": "/settings/parentsCourse",
                            "component": SettingParentsCourseMainPage,
                        },
                        {
                            "name": "강의실 별 학생 리스트",
                            "path": "/settings/classRoomCourse",
                            "component": SettingClassRoomCourseMainPage,
                        },
                    ]
                },
                {
                    "name": "강의 관리",
                    "path": "/settings/lecture",
                    "component": CourseLayout,
                    "children": [
                        {
                            "name": "강의 등록 수",
                            "path": "/settings/lecture/list",
                            "component": CourseLayout,
                            "children": [
                                {
                                    "name": "총 강의 등록 수",
                                    "path": "/settings/lecture/list/total",
                                    "component": CourseLayout,
                                },
                                {
                                    "name": "강사 별 총 강의 등록 수",
                                    "path": "/settings/lecture/list/lecturerTotal",
                                    "component": CourseLayout,
                                },
                                {
                                    "name": "강사, 강의 형태별 강의 등록 수",
                                    "path": "/settings/lecture/list/lecturerCategory",
                                    "component": CourseLayout,
                                },
                            ]
                        },
                    ]
                },
                {
                    "name": "매출내역",
                    "path": "/settings/payment/sales",
                    "component": SettingPaymentSalesPage,
                },
                {
                    "name": "정산내역",
                    "path": "/settings/payment/calculate",
                    "component": SettingPaymentCalculatePage,
                },
                {
                    "name": "결제 및 취소관리",
                    "path": "/settings/payment/payment",
                    "component": SettingPaymentPaymentPage,
                },
            ]
        },
        {
            "name": "공백",
            "path": "/blank",
            "component": BlankLayout,
            "children": [
                {
                    "name": "회원가입",
                    "path": "/sign-up",
                    "component": SignUpPage
                },
                {
                    "name": "회원가입 인증 성공",
                    "path": "/sign-up/checkplus-success",
                    "component": signUpCheckSuccessPage
                },
                {
                    "name": "회원가입 인증 실패",
                    "path": "/sign-up/checkplus-fail",
                    "component": signUpCheckFailPage
                },
                {
                    "name": "아이디,비밀번호 인증 성공",
                    "path": "/find-account/checkplus-success",
                    "component": findAccountCheckSuccessPage
                },
                {
                    "name": "아이디,비밀번호 인증 실패",
                    "path": "/find-account/checkplus-fail",
                    "component": findAccountCheckFailPage
                },
                {
                    "name": "카드결제 성공",
                    "path": "/mypage/users/payment/card-success",
                    "component": MypageUserPaymentCardSuccessPage,
                },
                {
                    "name": "카드결제 실패",
                    "path": "/mypage/users/payment/card-fail",
                    "component": MypageUserPaymentCardFailPage,
                },
                {
                    "name": "현금결제 성공",
                    "path": "/mypage/users/payment/money-success",
                    "component": MypageUserPaymentMoneySuccessPage,
                },
                {
                    "name": "현금결제 실패",
                    "path": "/mypage/users/payment/money-fail",
                    "component": MypageUserPaymentMoneyFailPage,
                },
                {
                    "name": "카카오페이 결제 성공",
                    "path": "/mypage/users/payment/kakao-success",
                    "component": MypageUserPaymentKakaoSuccessPage,
                },
                {
                    "name": "카카오페이 결제 실패",
                    "path": "/mypage/users/payment/kakao-fail",
                    "component": MypageUserPaymentKakaoFailPage,
                },
            ]
        },
        {
            "name": "ApplicationTest",
            "path": "/testpage",
            "redirect": "/algo",
            "component": MainLayout,
            "children": [
                {
                    "name": "추천알고리즘",
                    "path": "/algo",
                    "component": AlgoPage
                },
                {
                    "name": "크롤링뉴스",
                    "path": "/news",
                    "component": NewsPage
                },
                {
                    "name": "메인화면",
                    "path": "/test",
                    "component": TestPage
                },
                {
                    "name": "테스트화면",
                    "path": "/test2",
                    "component": Test2Page,
                    "props": true
                },
                {
                    "name": "테스트화면3",
                    "path": "/test3",
                    "component": Test3Page
                },
                {
                    "name": "테스트3지도API",
                    "path": "/test3/map",
                    "component": Test3MapApiPage
                },
                {
                    "name": "테스트3 크리에이터 마이페이지",
                    "path": "/test3/mypage-creator/main",
                    "component": Test3MypageCreatorMain
                },
                {
                    "name": "테스트3 크리에이터 마이페이지 수정",
                    "path": "/test3/mypage-creator/main-detail",
                    "component": Test3MypageCreatorMainDetail
                },
                {
                    "name": "테스트3 라이브 수정",
                    "path": "/test3/edit/main",
                    "component": Test3LiveEditPage
                },
                {
                    "name": "테스트3 강사 자료실",
                    "path": "/test3/lecturer/data",
                    "component": Test3LecturerDataPage
                },
                {
                    "name": "테스트3 강사 자료실 편집",
                    "path": "/test3/lecturer/data-detail",
                    "component": Test3LecturerDataMainDetail
                },
                {
                    "name": "테스트화면4",
                    "path": "/test4",
                    "component": Test4Page
                },
                {
                    "name": "공지화면테스트",
                    "path": "/boardTest",
                    "component": boardTest
                },
                {
                    "name": "차트테스트",
                    "path": "/toss/test",
                    "component": TossTestPage
                },
                {
                    "name": "공지화면상세페이지테스트",
                    "path": "/boardTest/detail",
                    "component": boardTestDetail
                },
                {
                    "name": "차트테스트",
                    "path": "/chartTest",
                    "component": chartTest
                },
                {
                    "name": "학생 리스트",
                    "path": "/studentList",
                    "component": StudentList
                },
                {
                    "name": "학생 상세",
                    "path": "/studentList/detail",
                    "component": StudentListDetail
                },
                {
                    "name": "학부모 등록",
                    "path": "/parentsTest",
                    "component": ParentsTest
                },
            ]
        },
    ]
});
/* beforeEach */
router.beforeEach(async function (to, from, next) {
    var token,
        treeMenuList,
        treeMenu,
        regex,
        authenticated,
        mainTitle,
        subTitle,
        title,
        titlePath,
        i;
    mainTitle = "Suppor-T";
    regex = to.matched.length > 0 ? to.matched[to.matched.length - 1].regex : null;

    token = meta.auth.getToken();
    if (await meta.auth.authenticated(token)) {
        if (!store.state.app.token) {
            meta.auth.authorize(token);
        }

        const session = (await axios({"url": "/api/common/menus/session/" + store.state.app.user.id, "method": "get"})).data;
        store.commit("app/SET_MENU_LIST", session.menuList);
        store.commit("app/SET_TREE_MENU_LIST", session.treeMenuList);

        treeMenuList = session.treeMenuList;
        authenticated = false;
        for (i = 0; i < treeMenuList.length; i++) {
            treeMenu = treeMenuList[i];
            if (regex && regex.test(treeMenu.path)) {
                authenticated = true;
                break;
            }
        }

        titlePath = [];
        for (i = 0; i < treeMenuList.length; i++) {
            treeMenu = treeMenuList[i];
            if (new RegExp("^" + treeMenu.path + ".*", "g").test(to.path)) {
                titlePath.push(treeMenu.name);
            }
        }

        if (to.path.startsWith("/sign-up/checkplus-success")) {
            authenticated = true;
        }

        store.state.app.showMenuPathName = titlePath.join(" > ");
        titlePath = titlePath.reverse();
        title = titlePath.join(" < ");
        if (authenticated) {
            document.title = title;
            next();
        } else {
            router.push({"path": "/"});
        }
    } else {
        var isLogin = false;
        var layout;
        meta.auth.unauthorize();

        if (to.path.startsWith("/sign-up")) {
            subTitle = "회원가입";
            titlePath = [subTitle, mainTitle];
            title = titlePath.join(" < ");
            document.title = title;
            next();
        }  else if (to.path.startsWith("/find-account")) {
            subTitle = "계정찾기";
            titlePath = [subTitle, mainTitle];
            title = titlePath.join(" < ");
            document.title = title;
            next();
        }

        treeMenuList = (await meta.api.util.menu.getDefaultMenuList()).data.items;
        titlePath = ["Suppor-T"];
        if(to.path === "/"){
            titlePath.push("메인화면");
            layout = window.app.$children.find(e=>e.selection !== undefined);
            if(layout !== undefined) {
                layout.selection = null;
            }
        }
        for (i = 0; i < treeMenuList.length; i++) {
            treeMenu = treeMenuList[i];
            if (treeMenu.path === to.path) {
                if(treeMenu.publicyStatus === "F") {
                    await meta.alert("로그인이 필요합니다.");
                    layout = window.app.$children.find(e=>e.selection !== undefined);
                    if(layout !== undefined) {
                        layout.selection = null;
                    }
                    isLogin = true;
                } else {
                    titlePath.push(treeMenu.name);
                }
            }
        }
        if(isLogin) {
            router.push({"path": "/main", "query": {"redirectURL": to.fullPath, "loginPopDialog": true}});
        } else {
            if(titlePath.length === 1) {
                router.push({"path": "/"});
            } else {
                store.state.app.showMenuPathName = titlePath.join(" > ");
                titlePath = titlePath.reverse();
                title = titlePath.join(" < ");
                document.title = title;
                next();
            }
        }
    }
});