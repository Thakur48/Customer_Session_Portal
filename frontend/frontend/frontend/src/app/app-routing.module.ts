import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { LoginComponent } from './features/login/login.component';
import { AccessControlGuard } from './features/guard/access-control.guard';
import { NewSessionComponent } from './features/new-session/new-session.component';
import { EditSessionComponent } from './features/edit-session/edit-session.component';
const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path:'login', component:LoginComponent},
  {path: 'home',component: DashboardComponent,} ,
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path:'newsession', component:NewSessionComponent},
  {path:'editsession', component:EditSessionComponent}
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
