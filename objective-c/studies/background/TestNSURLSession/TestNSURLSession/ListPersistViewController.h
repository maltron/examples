//
//  PersistentViewController.h
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/1/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

#import "EntityPersistent.h"

@interface ListPersistViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, NSFetchedResultsControllerDelegate, EntityPersistent>
@property (nonatomic, strong) UITableView *tableView;

@end
