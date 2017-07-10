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
    public golaurl = 'https://goladev.mobigraph.co/ginger/';   
    public giphyurl = 'http://api.giphy.com/v1/gifs/trending?api_key=dc6zaTOxFJmzC';
    public serverUrl : string;
    public headers;
    public options;
    constructor(private http : Http,
                private _config : Configuration){
                    this.getUrl();
                    this.setHeader();
                    this.getHeader();
                }


    getUrl(){
        this.url = this._config.baseUrl;
    }            

    setHeader() {
        this.headers = new Headers({
        'Content-Type' : 'application/json',
        'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w'
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

     public getTrendingGifs(idiom,pageno){
		return this.http.get(this.golaurl+idiom+"/gifs/" + pageno + "/18", this.options)
			   .map(this.extractData)
			   .catch(this.handleError);
	}

      //using mobigraph api
      public getRelatedGifs(idiom, id){
          return this.http.get(this.golaurl+idiom+"/relatedGifs/"+id+"/0/20",this.options)
                    .map(this.extractData)
                    .catch(this.handleError)
      }

     public mainTabs(){
            return this.http.get('assets/layout.json',this.options)
                    .map(this.extractData)
                    .catch(this.handleError)
     }

    public getTabCategories(idiom){
       return this.http.get(this.golaurl+'tabs/'+idiom,this.options)
                .map(this.extractData)
                .catch(this.handleError)
    }

    public getTabDataviaTabId(idiom,tabid){
        return this.http.get(this.golaurl+idiom+'/topItems/'+tabid+'/0/4',this.options)
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