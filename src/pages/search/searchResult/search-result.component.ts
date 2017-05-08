import { Component , OnInit } from '@angular/core';
import {NavParams} from 'ionic-angular';

@Component({
    selector : 'page-search-result',
    templateUrl: 'search-result.html'
})


export class SearchResultComponent implements OnInit {

    public searchItem;
    constructor(private navparams : NavParams){
        this.searchItem = this.navparams.get('sitem');
    }
    ngOnInit(){
        
    }

    getItems(){
        return this.searchItem;
    }
    
} 