import { Component } from '@angular/core';
import { NavController,NavParams} from 'ionic-angular';
import { SearchResultComponent } from './searchResult/search-result.component';


@Component({
    selector : 'page-search',
    templateUrl : 'search.html'
})


export class SearchComponent{

    searchQuery : string = '';
    items : string[];

    constructor(private navCtrl : NavController,
                private navParmas : NavParams){
       // this.initializeitems();
    }

    initializeitems(){
        this.items = [
            'Happy',
            'Happy Songs',
            'Random gifs',
            'Holla',
            'Hell'
        ];
    }


    getItems(ev : any){
        this.initializeitems();

        let val = ev.target.value;
         if (val && val.trim() != '') {
      this.items = this.items.filter((item) => {
        return (item.toLowerCase().indexOf(val.toLowerCase()) > -1);
      })
    }
}

TagsClick(item){
    console.log('search gif with tag name',item);
    this.navCtrl.push(SearchResultComponent,{
        'sitem' : item 
    });
}
}