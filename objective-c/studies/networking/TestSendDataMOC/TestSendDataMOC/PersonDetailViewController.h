//
//  PersonDetailViewController.h
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/18/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EntityDetailViewController.h"
#import "PersonCRUD.h"
#import "Person.h"

@interface PersonDetailViewController : EntityDetailViewController<UITableViewDataSource>

-(id)initWithCRUD:(PersonCRUD *)crud;

-(void)newPerson;
-(void)editPerson:(Person *)person;

@end
