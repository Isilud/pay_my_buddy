import axios from "axios";

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post["Content-Type"] = 'application/json';

export async function fetchMethod<T>(method?:string, url?:string, data?:any, token?:string):Promise<T> {
    console.log(method, url, data, token)
    const headers = token ?  { Authorization: `Bearer ${token}` } : undefined ;
    return await axios({method, url, data, headers}).then((res)=>res.data)
}