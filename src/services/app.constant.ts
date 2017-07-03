import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import { Headers, RequestOptions, Http, Response } from '@angular/http';
import { Events } from 'ionic-angular';
import { Observable } from 'rxjs/Observable';
import { CustomService } from './custom.service';

@Injectable()
export class Configuration {

    public headers;
    public options;
    constructor(){}

    public baseUrl : string = "https://grey.mobigraph.co/ginger/";
   
    getAuthToken(){
        let token = localStorage.getItem('auth_token');
        return token;
    }  

    setHeader() {
        this.headers = new Headers({
        'Content-Type' : 'application/json',
        'Authorization' : 'Bearer ' + localStorage.getItem("auth_token")
        });
           this.options = new RequestOptions({
           headers : this.headers
        });

    }

    getHeader() {
        return this.options;
    } 
   
}