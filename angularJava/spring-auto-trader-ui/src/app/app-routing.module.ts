import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TradeListComponent } from './trade-list/trade-list.component';
 
const routes: Routes = [
  { path: 'trades', component: TradeListComponent },
];
 
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
