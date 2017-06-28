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
                    this.setHeader();
                }

    getUrl(){
        this.url = this._config.baseUrl;
        console.log('localstorage',localStorage.getItem('access_token'));
    }            

    setHeader() {
        this.headers = new Headers({
            'Content-Type' : 'application/json',
            'Authorization' : 'Bearer'+' '+ localStorage.getItem('access_token'),
            'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w'
        });
        this.options = new RequestOptions({
         headers : this.headers
        });
    }

    getHeader() {
        return this.options;
    }

    UploadGifsByUrl(body){
   
		return this.http.post('https://violet.mobigraph.co/ginger/uploadFromUrl',body, this.options)
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