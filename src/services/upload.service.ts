import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';
import {Configuration} from './app.constant';

@Injectable()
export class UploadGifService{

    public url:string;
    public serverUrl : string;
    public headers;
    public options;
    constructor(private http : Http,
                private _config : Configuration){
                    this.getUrl();
                    this.getHeader();
                }

    getUrl(){
        this.url = this._config.baseUrl;
    }            

    setHeader() {
        this.headers = new Headers({
            'Content-Type' : 'application/json',
            'Authorization' : 'Bearer' + 'localstorage.getItem()'
        });
        this.options = new RequestOptions({
         headers : this.headers
        });
    }

    getHeader() {
        return this.options;
    }

    UploadGifs(){
        let file = {
            "tags": "[Water , Tired]",
            "categories" : "Tired",
            "gif" : "file_name",
            "idom" : "hindi"
        }
		return this.http.post(this.url + 'uploadGif', this.options)
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