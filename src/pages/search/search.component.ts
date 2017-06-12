import { Component,Input,ViewChild } from '@angular/core';
import { NavController,NavParams} from 'ionic-angular';
import { SearchResultComponent } from './searchResult/search-result.component';
import { SearchService } from '../../services/search.service'; 
import { Keyboard } from '@ionic-native/keyboard';

@Component({
    selector : 'page-search',
    templateUrl : 'search.html'
})

export class SearchComponent{

    searchQuery : string = '';
    items : string[];
    @ViewChild('search') myInput ;
    public TopSearchlist ;

    constructor(private navCtrl : NavController,
                private navParmas : NavParams,
                private _searchservice : SearchService,
                private keyboard : Keyboard){
                // this.initializeitems();
                //  this.getSuggestedItems(text);
                this.TopSearchlist = true;
    }

    ionViewDidLoad(){
        setTimeout(() => {
            this.myInput.setFocus();
            this.keyboard.show();
        }, 1000);
    }

    getItems(ev : any){
        
        let val = ev.target.value;
        this.getSuggestedItems(val);
            this.TopSearchlist = false;
            if(val.trim() == ''){
                this.TopSearchlist = true;
            }
         if (val && val.trim() != '') {
             this.suggestedText = this.suggestedText.filter((item) => {
            return (item.toLowerCase().indexOf(val.toLowerCase()) > -1);
          })
        }
     }


    public suggestedText = [];
    getSuggestedItems(val){
        this._searchservice.TextSuggestions(val)
        .subscribe( (data) => { this.suggestedText = data },
                    () =>  console.log('suggested text', this.suggestedText))
    }
    

    topsearcheslist = ['affhj','bffr','cff','dfff','efdfr'];
    topsearches(){
        this._searchservice.TopSearchesList()
            .subscribe( (data) => { this.topsearcheslist = data },
                        (err) => { console.log(err)},
                        () => console.log(this.topsearcheslist))
    }


    public searchedGifs = [];
    getSearchGifs(item){
        this._searchservice.GetGifsSearch(item)
        .subscribe( (res) => { this.searchedGifs = res },
                    () => console.log('related gifs',this.searchedGifs))
    }

    someFunction(event: KeyboardEvent) { 
        let val = (<HTMLInputElement>event.target).value;
        this.getSearchGifs(val);

        this.navCtrl.push(SearchResultComponent,{
            'sitem' : val ,
            'relatedgifs' :  this.searchedGifs
      });
    }


    TagsClick(item){
         console.log('search gif with tag name',item);
         this.getSearchGifs(item);
         
         this.navCtrl.push(SearchResultComponent,{
            'sitem' : item ,
            'relatedgifs' :  this.searchedGifs
      });
    }
}