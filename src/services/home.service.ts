import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';
import { Configuration } from './app.constant';


@Injectable()
export class HomeService{

    public url;
    public giphyurl = 'http://api.giphy.com/v1/gifs/trending?api_key=dc6zaTOxFJmzC';
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
        'Content-Type' : 'application/json'
        });
        this.options = new RequestOptions({
         headers : this.headers
        });
    }

    getHeader() {
        return this.options;
    }

    // getTrendingGifs(idiom){
    //     console.log('idiom3',idiom);
        
	// 	return this.http.get(this.url+idiom+'/gifs', this.options)
	// 		   .map(this.extractData)
	// 		   .catch(this.handleError);
	// }

     public getTrendingGifs(idiom){
        console.log('idiom3',idiom);
        
		return this.http.get(this.giphyurl, this.options)
			   .map(this.extractData)
			   .catch(this.handleError);
	  }

     public mainTabs(){
            return this.http.get('assets/layout.json',this.options)
                    .map(this.extractData)
                    .catch(this.handleError)
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