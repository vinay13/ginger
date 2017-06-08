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
     // this.setHeader();
    }

    // setHeader() {
    //     this.headers = new Headers({
    //       "Content-Type" : "application/json"
    //     });
    //     this.options = new RequestOptions({
    //      headers : this.headers
    //     });
    // }

    getHeader(){
        return this.options;
    }

    public verifyUser(body){
       let bodyString = JSON.parse(JSON.stringify(body));
        console.log('bs',bodyString);
        console.log('bs',body);
        let headers = new Headers({
          "Content-Type" : "application/json"
        });
        let options = new RequestOptions({
         headers : this.headers
        });
       return this.http.post(this.baseUrl+'/signin',bodyString,options)
                        .map((res:Response) => res.json()) 
                         .catch((error:any) => Observable.throw(error.json().error || 'Server error')); 
    }



    public gAuthCallback(body){
         console.log('gauthbody',body);
         let headers = new Headers({
          "Content-Type" : "application/json"
        });
        let options = new RequestOptions({
         headers : this.headers
        });
      return this.http.post(this.baseUrl+"/gauth/oauth2callback?code="+body+"&state="+"45",options)
                .map((res : Response) => res.json())
                .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
    }



    // verifyUser(data): Observable<any[]> {
    //     return this.http.post(this.baseUrl + "/oauth/token?grant_type=password&username="+data.username+"&password="+data.password, {})
    //                 .map(this.extractData)
    //                 .catch(this.handleError);
    // }

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