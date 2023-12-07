import Index from "views/Index.js";
import Profile from "views/examples/Profile.js";
import Maps from "views/examples/Maps.js";
import Register from "views/examples/Register.js";
import Login from "views/examples/Login.js";
import Lists from "views/examples/List.js";
import Coupons from "views/examples/Coupon.js"
import Icons from "views/examples/Icons.js";


var routes = [
  {
    path: "/index",
    name: "Dashboard",
    icon: "ni ni-tv-2 text-primary",
    component: <Index />,
    layout: "/admin",
    requireToken: false,
  },
  {
    path: "/maps",
    name: "Maps",
    icon: "ni ni-pin-3 text-orange",
    component: <Maps />,
    layout: "/admin",
    requireToken: true,
  },
  {
    path: "/lists",
    name: "Lists",
    icon: "ni ni-bullet-list-67 text-red",
    component: <Lists />,
    layout: "/admin",
    requireToken: true,
  },
  {
    path: "/user-profile",
    name: "User Profile",
    icon: "ni ni-single-02 text-yellow",
    component: <Profile />,
    layout: "/admin",
    requireToken: true,
  },
  {
    path: "/coupons",
    name: "Coupons",
    icon: "ni ni-single-02 text-yellow",
    component: <Coupons />,
    layout: "/admin",
    requireToken: true,
  },
  {
    path: "/icons",
    name: "Icons",
    icon: "ni ni-planet text-blue",
    component: <Icons />,
    layout: "/admin",
    requireToken: true,
  },
  {
    path: "/login",
    name: "Login",
    icon: "ni ni-key-25 text-info",
    component: <Login />,
    layout: "/auth",
    requireToken: false,
  },
  {
    path: "/register",
    name: "Register",
    icon: "ni ni-circle-08 text-pink",
    component: <Register />,
    layout: "/auth",
    requireToken: false,
  },
];
export default routes;
