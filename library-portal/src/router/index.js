import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login"
import Home from "@/views/Home";
import Class from "../views/category/Class";
import RenderCategory from "../views/category/RenderCategory";
import Area from "../views/category/Area";
import Book from "../views/book/Book";
import Reader from "../views/reader/Reader";
import RenderHistory from "../views/book/RenderHistory";
import LenderHistory from "../views/lender/LendHistory";
import Datav from "../views/datav/Datav";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login
    },
    {
        path: '/home',
        name: '类目管理',
        component: Home,
        children: [
            {
                path: '/class',
                name: '图书类别',
                component: Class
            },
            {
                path: '/renderCategory',
                name: '读者类别',
                component: RenderCategory
            },
            {
                path: '/area',
                name: '图书馆区域',
                component: Area
            }
        ]
    },
    {
        path: '/home',
        name: '图书管理',
        component: Home,
        children: [
            {
                path: '/book',
                name: '图书管理',
                component: Book
            },
            {
                path: '/renderHistory',
                name: '借阅历史',
                component: RenderHistory
            }
        ]
    },
    {
        path: '/home',
        name: '读者管理',
        component: Home,
        children: [
            {
                path: '/reader',
                name: '读者管理',
                component: Reader
            },
        ]
    },
    {
        path: '/home',
        name: '借阅管理',
        component: Home,
        children: [
            {
                path: '/book',
                name: '借阅管理',
                component: Book
            },
            {
                path: '/lendHistory',
                name: '借阅历史',
                component: LenderHistory
            }
        ]
    },
    {
        path: '/home',
        name: '报表统计',
        component: Home,
        children: [
            {
                path: '/datav',
                name: '报表统计',
                component: Datav
            },
        ]
    }

]

// 管理员菜单
const adminRoutesInfo = [
    {
        path: '/home',
        name: '类目管理',
        iconCls: 'fa fa-flask',
        children: [
            {
                path: '/class',
                name: '图书类别',
            },
            {
                path: '/renderCategory',
                name: '读者类别',
            },
            {
                path: '/area',
                name: '图书馆区域',
            }
        ]

    },

    {
        path: '/home',
        name: '图书管理',
        iconCls: 'fa fa-book',
        children: [
            {
                path: '/book',
                name: '图书管理'
            },
            {
                path: '/renderHistory',
                name: '借阅历史',
            }
        ]
    },
    {
        path: '/home',
        name: '读者管理',
        iconCls: 'fa fa-user',
        children: [
            {
                path: '/reader',
                name: '读者管理'
            }
        ]
    },
    {
        path: '/home',
        name: '报表统计',
        iconCls: 'fa fa-bar-chart',
        children: [
            {
                path: '/datav',
                name: '报表统计'
            }
        ]
    }
]

// 普通用户菜单
const routesInfo = [
    {
        path: '/home',
        name: '借阅管理',
        iconCls: 'fa fa-book',
        children: [
            {
                path: '/book',
                name: '图书管理'
            },
            {
                path: '/lendHistory',
                name: '借阅历史'
            }
        ]
    },
]


const router = new VueRouter({
    routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
    if (sessionStorage.getItem('tokenStr')) {
        //普通用户
        if (!sessionStorage.getItem('routes') && JSON.parse(sessionStorage.getItem('user')).readerCategoryId) {
            sessionStorage.setItem('routes', JSON.stringify(routesInfo))
        }
        //管理员
        if (!sessionStorage.getItem('adminRoutes') && !(JSON.parse(sessionStorage.getItem('user')).readerCategoryId)) {
            sessionStorage.setItem('routes', JSON.stringify(adminRoutesInfo))
        }
        //console.log(router)
        // 如果用户不存在
        /*if (!sessionStorage.getItem('user')) {
            // 判断用户信息是否存在
            getRequest('/userinfo').then(resp => {
                if (resp.object) {
                    // 存入用户信息，转字符串，存入 sessionStorage
                    sessionStorage.setItem('user', JSON.stringify(resp.object))
                    // 同步用户信息 编辑用户
                    // store.commit('INIT_ADMIN',resp)
                }
            })
        }*/
        next();
    } else {
        if (to.path === '/') {
            next()
        } else {
            next('/?redirect=' + to.path)
        }
    }
})

export default router