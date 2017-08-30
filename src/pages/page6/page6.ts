import { Component,ViewChild } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchResultComponent } from '../search/searchResult/search-result.component';
import { MasonryModule,AngularMasonry } from 'angular2-masonry';

@Component({
  selector: 'page-page6',
  templateUrl: 'page6.html'
})

export class Page6Page {
   rootNavCtrl: NavController;
   selectedIdiom = localStorage.getItem('idiom');
    @ViewChild(AngularMasonry) public masonry: AngularMasonry;
   constructor(public navparams : NavParams,
                public _homeserv : HomeService){
                    this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                //  this.newselectedIdiom = this.navparams.data;
                //  this.selectedIdiom = this.newselectedIdiom.idiom;
                    this.tabcat();
            }

    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[4].id);  },
                                (err) => { console.log(err)},
                                () => { })
    }

    public tabIddata;
    public gifs;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ;this.textonGIFs(); this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('page5data',this.tabIddata ))
        }


  ng_class;
  click_func= "EmotionClicked" ;
  textonGIFs(){
let click_func;
      console.log('text1',this.tabIddata[0]['text']);
      console.log('text2',this.tabIddata[0].text);
    if(this.tabIddata[0].text != '' && this.tabIddata[0]['text']){
    
        this.ng_class =  'wrapper';
     
    }
    else{
      this.ng_class = 'wrapper2';

    }
  }

    EmotionClicked(tag){
      console.log('tag',tag);
      console.log('idiom',this.selectedIdiom);
      this.rootNavCtrl.push(SearchResultComponent,{
            'tag' : tag,
            'idiom': this.selectedIdiom
      });
  }

     public newlayout() {
      setTimeout(() => {
            this.masonry._msnry.layout();
      },1000);

      // console.log('AngularMasonry:', 'Layout');
    }

    ionViewWillEnter (){
      this.newlayout();
    }

    }