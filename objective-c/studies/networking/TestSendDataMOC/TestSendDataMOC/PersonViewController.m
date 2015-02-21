//
//  PersonViewController.m
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/18/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonViewController.h"
#import "AppDelegate.h"
#import "Person.h"
#import "PersonCRUD.h"
#import "PersonDetailViewController.h"

@interface PersonViewController ()
@property (readonly, nonatomic, strong) NSFetchedResultsController *fetchedResultsController;
@property (nonatomic, strong) UIBarButtonItem *buttonFetchAll, *buttonAdd;
@property (nonatomic, strong) PersonCRUD *crud;
@property (readonly, nonatomic, strong) PersonDetailViewController *detail;

@end

@implementation PersonViewController
@synthesize fetchedResultsController = _fetchedResultsController;
@synthesize crud = _crud;
@synthesize buttonFetchAll = _buttonFetchAll, buttonAdd = _buttonAdd;
@synthesize detail = _detail;

-(id)initWithCRUD:(PersonCRUD *)crud {
    self = [super init];
    if(self) {
        _crud = crud;
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonFetchAll = [[UIBarButtonItem alloc] initWithTitle:@"Fetch" style:UIBarButtonItemStylePlain target:self action:@selector(actionFetchAll:)];
    _buttonAdd = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(actionAdd:)];
    [self.navigationItem setLeftBarButtonItem:_buttonFetchAll];
    [self.navigationItem setRightBarButtonItem:_buttonAdd];
    [self.navigationItem setTitle:@"Person"];
}

-(void)actionFetchAll:(id)sender {
    
}

-(void)actionAdd:(id)sender {
    PersonDetailViewController *detail = [self detail];
    [detail newPerson];
    
    [self.navigationController pushViewController:detail animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(PersonDetailViewController *)detail {
    if(_detail) return _detail;
    
    _detail = [[PersonDetailViewController alloc] initWithCRUD:self.crud];
    return _detail;
}

// ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT
//   ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    NSManagedObjectContext *context = [[AppDelegate instance] mainContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[Person entity:context]];
    [fetchRequest setFetchBatchSize:20];
    
    NSSortDescriptor *sortFirst = [[NSSortDescriptor alloc] initWithKey:@"firstName" ascending:YES];
    NSSortDescriptor *sortLast = [[NSSortDescriptor alloc] initWithKey:@"lastName" ascending:YES];
    [fetchRequest setSortDescriptors:@[sortFirst, sortLast]];
    
    NSFetchedResultsController *temp = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:context sectionNameKeyPath:nil cacheName:@"CACHE_PERSON"];
    temp.delegate = self;
    _fetchedResultsController = temp;
    
    NSError *error = nil;
    if(![self.fetchedResultsController performFetch:&error]) {
        NSLog(@"### PersonViewController fetchedResultsController] ERROR:%@, %@", error, [error userInfo]);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Person *person = (Person *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@", [person firstName], [person lastName]];
}


@end
