import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';
import { Configuration } from './app.constant';

@Injectable()
export class NotificationService{

    constructor(private http : Http){}
    golaurl = 'https://goladev.mobigraph.co/ginger/';  

    public notificationDeviceId(){
        let headers = new Headers({
            'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
            'Content-Type' : 'application/json',
         });
        
        let options = new RequestOptions({
             headers : headers
        });

        let body = {
                 "deviceId":"1",
                 "os":"android",
                 "osVersion":"5.1",
                 "deviceToken":"Asdfasdf"
        }

        return this.http.post(this.golaurl+'user/device',options)
                  .map(this.extractData)
                  .catch(this.handleError)
    }


    private extractData(res: Response) {
		if (res.status === 204) { return res; }
		let body = res.json();
        console.log('data',body);
		return body || { };
	}

	private handleError(error: Response | any){
        let errMsg: string;
        if (error instanceof Response) {
        errMsg = `${error.status} - ${error.ok || ''}`;
        if (error.status === 0) {
            errMsg = `${error.status} - "No Internet"`;
        }
        } 
        else {
            errMsg = error.message ? error.message : error.toString();
        }
      return Observable.throw(errMsg);
    }
}