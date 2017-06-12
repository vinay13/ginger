import {Component,OnInit,Input,ViewChild} from '@angular/core';
import {NavParams,NavController} from 'ionic-angular';
import {SearchService} from '../../../services/search.service';
import {CustomService} from '../../../services/custom.service';
import {GifDetailComponent} from '../../home/gifdetail/gifdetail.component';

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
                private cs : CustomService ,
                private navCtrl : NavController){ 
                } 

    getSearchGifs(item){
        this.cs.showLoader();
        this._searchService.GetGifsSearch(item)
        .subscribe( (res) => { this.searchedGifs = res; this.cs.hideLoader();  },
                    (err) => { console.log(err); this.cs.hideLoader();},
                    () => console.log('related gifs',this.searchedGifs))
    }

    ngOnInit(){
         this.searchItem = this.navparams.get('sitem') || this.navparams.get('tag');
         this.getSearchGifs(this.searchItem);
      //  this.searchedGifs = this.navparams.get('relatedgifs');
      //  console.log('seeergifs',this.searchedGifs);
    }

    someFunction(event: KeyboardEvent) { 
        let val = (<HTMLInputElement>event.target).value;
        this.getSearchGifs(val);

    //     this.navCtrl.push(SearchResultComponent,{
    //         'sitem' : val ,
    //         'relatedgifs' :  this.searchedGifs
    //   });
    }

    getItems(){
        return this.searchItem;
    }

    viewGif(url){
        this.navCtrl.push(GifDetailComponent,{
            'url': url
        });
    } 

} 