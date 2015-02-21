//
//  PersonDetailViewController.h
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EntityDetailViewController.h"
#import "Person.h"

@interface PersonDetailViewController : EntityDetailViewController<UITableViewDataSource>
@property (nonatomic, strong) NSNumber *personID;

-(void)newPerson;
-(void)editPerson:(Person *)person;

@end
