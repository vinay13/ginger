import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';


@Injectable()
export class LoginService{

    public baseUrl : string = "https://violet.mobigraph.co/ginger";
    public serverUrl : string;
    public headers;
    public options;
    constructor(private http : Http){
     
    }


    getHeader(){
        return this.options;
    }

    public verifyUser(body){

        console.log('bs',body);
      
        let headers = new Headers({
          'Content-Type': 'application/json'
        });
        let options = new RequestOptions({
           headers : this.headers
        });
       return this.http.post( 'api/signin',body,options)
               .map(this.extractData)
               .catch(this.handleError);            
    }

    public gAuthCallback(body){
         alert(body);
         let encodedbody = encodeURIComponent(body);
       //  let bodyString = JSON.parse(JSON.stringify(body));
         let headers = new Headers({
          'Content-Type': 'application/json'
        });
        let tbody = {};
        let options = new RequestOptions({
         headers : this.headers
        });
      return this.http.post(this.baseUrl+"/gauth/oauth2callback?code="+body+"&state="+"55",tbody,this.options)
               .map(this.extractData)
               .catch(this.handleError);
    }

    private extractData(res: Response) {
      if (res.status === 204) { return res; }
      let body = res.json();
          console.log('data',body);
      return body || { };
    }

    private handleError(error: Response | any) {
      let errMsg: string;
      if (error instanceof Response) {
        errMsg = `${error.status} - ${error.ok || ''}`;
        if (error.status === 0) {
          errMsg = `${error.status} - "No Internet"`;
        }
      } else {
        errMsg = error.message ? error.message : error.toString();
      }
      return Observable.throw(errMsg);
    }

}