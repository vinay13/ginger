import {Component,OnInit} from '@angular/core';
import {NavParams} from 'ionic-angular';
import {SearchService} from '../../../services/search.service';
import {CustomService} from '../../../services/custom.service';

@Component({
    selector : 'page-search-result',
    templateUrl: 'search-result.html',
   // styleUrls : ['./search-result.scss']
})


export class SearchResultComponent implements OnInit {

    public searchItem;
    public searchedGifs = [];
    constructor(private navparams : NavParams,
                private _searchService : SearchService,
                private cs : CustomService ){
       
    }


    getSearchGifs(item){
        this.cs.showLoader();
        this._searchService.GetGifsSearch(item)
        .subscribe( (res) => { this.searchedGifs = res; this.cs.hideLoader(); },
                    (err) => { this.cs.hideLoader();},
                    () => console.log('related gifs',this.searchedGifs))
    }

    ngOnInit(){
         this.searchItem = this.navparams.get('sitem') || this.navparams.get('tag');
         this.getSearchGifs(this.searchItem);
      //  this.searchedGifs = this.navparams.get('relatedgifs');
      //  console.log('seeergifs',this.searchedGifs);
    }

    getItems(){
        return this.searchItem;
    }
    
} 