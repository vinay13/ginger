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
        'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
        'GOLA_USER_TIME_ZONE': new Date().toTimeString()
        });
        this.options = new RequestOptions({
         headers : this.headers
        });
    }

    getHeader() {
        return this.options;
    }

     public getTrendingGifs(idiom,pageno){
		return this.http.get(this.golaurl+idiom+"/gifs/" + pageno + "/18", this.options)
			   .map(this.extractData)
			   .catch(this.handleError);
	}

      //using mobigraph api
      public getRelatedGifs(idiom, id,pageno){
          return this.http.get(this.golaurl+idiom+"/relatedGifs/"+id+"/"+pageno+"/10",this.options)
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
        return this.http.get(this.golaurl+idiom+'/topItems/'+tabid+'/0/2',this.options)
                .map(this.extractData)
                .catch(this.handleError)
    }

    public getGifviaID(gifid){
         let headers = new Headers({
            'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
            'Content-Type' : 'application/json',
         });
        
        let options = new RequestOptions({
             headers : headers
        });

        return this.http.get(this.golaurl+'gifs/metaData/'+ gifid,options)
                .map(this.extractData)
                .catch(this.handleError)
    }

    public favoritesGifs(gifId){
         let headers = new Headers({
            'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
            'Authorization': 'Bearer' + ' ' + localStorage.getItem('access_token')
        });
        let options = new RequestOptions({
             headers : headers
        });
        return this.http.post(this.golaurl+'gifs/favourite/'+ gifId, {}, options)
    }


    // public sharesdetails(gifId){
    //     let headers = new Headers({
    //         'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
    //         'Content-Type' : 'application/json',
    //         'GOLA_USER_TIME_ZONE': new Date().toTimeString()
    //     });
             
    //     let options = new RequestOptions({
    //          headers : headers
    //     });

    //     return this.http.get(this.golaurl+'gifs/metadata/'+ gifId,options)
    //         .map(this.extractData)
    //         .catch(this.handleError)
    // } 


    public shareArbit(gifId){
        let headers = new Headers({
            'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w'
        });
             
        let options = new RequestOptions({
             headers : headers
        });

        return this.http.post(this.golaurl+'share/gif/'+gifId,{},options)
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