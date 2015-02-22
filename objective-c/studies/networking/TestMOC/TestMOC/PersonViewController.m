//
//  PersonViewController.m
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonViewController.h"
#import "Person.h"
#import "AppDelegate.h"
#import "PersonDetailViewController.h"
#import "PersonService.h"

@interface PersonViewController ()
@property (readonly, nonatomic, strong) NSFetchedResultsController *fetchedResultsController;
@property (nonatomic, strong) UIBarButtonItem *buttonFetch, *buttonAdd;
@property (readonly, nonatomic, strong) PersonDetailViewController *detail;
@property (readonly, nonatomic, strong) PersonService *service;

@end

@implementation PersonViewController
@synthesize fetchedResultsController = _fetchedResultsController;
@synthesize detail = _detail;
@synthesize service = _service;

-(id)init {
    self = [super init];
    if(self) {
        _service = [[PersonService alloc] init];
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonFetch = [[UIBarButtonItem alloc] initWithTitle:@"Fetch" style:UIBarButtonItemStylePlain target:self action:@selector(actionFetch:)];
    _buttonAdd = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(actionAdd:)];
    [self.navigationItem setLeftBarButtonItem:_buttonFetch];
    [self.navigationItem setRightBarButtonItem:_buttonAdd];
    [self.navigationItem setTitle:@"Person"];
}

-(void)actionAdd:(id)sender {
    PersonDetailViewController *detail = [self detail];
    [detail newPerson];
    
    [self.navigationController pushViewController:detail animated:YES];
}

-(void)actionFetch:(id)sender {
    [self.service fetchAll:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
        
    }];
}

-(PersonDetailViewController *)detail {
    if(_detail) return _detail;
    
    _detail = [[PersonDetailViewController alloc] init:[self service]];
    return _detail;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    NSLog(@"*** [PersonVC didReceiveMemoryWarning]");
}

// TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE
//  TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@">>> [PersonVC commitEditingStyle]");
    
    if(editingStyle == UITableViewCellEditingStyleDelete) {
        NSLog(@">>> [PersonVC commitEditingStyle] UITableViewCellEditingStyleDelete");
        [self.service delete:[self.fetchedResultsController objectAtIndexPath:indexPath]
           context:[self.fetchedResultsController managedObjectContext] 
           completionHandler:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
            
            if([self.service isSuccessfull:response]) {
                
            }
            
        }];
        
        
//        NSManagedObjectContext *context = [self.fetchedResultsController managedObjectContext];
//        [context deleteObject:[self.fetchedResultsController objectAtIndexPath:indexPath]];
//        
//        NSError *error = nil;
//        if(![context save:&error]) {
//            // Replace this implementation with code to handle the error
//            // apropriately. abort() causes the application to generate
//            // a crash log and terminate. You should not use this function
//            // in a shipping application, altough it may be useful
//            // during development.
//            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
//            abort();
//        }
    }
}

// TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE
//  TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    PersonDetailViewController *detail = [self detail];
    [detail editPerson:[self.fetchedResultsController objectAtIndexPath:indexPath]];
    
    [self.navigationController pushViewController:detail animated:YES];
}


// ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY
//  ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    NSManagedObjectContext *context = [[AppDelegate instance] mainContext];
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[Person entity:context]];
    [fetchRequest setFetchBatchSize:20];
    
    NSSortDescriptor *sortFirst = [NSSortDescriptor sortDescriptorWithKey:@"firstName" ascending:YES];
    NSSortDescriptor *sortLast = [NSSortDescriptor sortDescriptorWithKey:@"lastName" ascending:YES];
    [fetchRequest setSortDescriptors:[NSArray arrayWithObjects:sortFirst, sortLast, nil]];
    
    _fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:context sectionNameKeyPath:nil cacheName:@"CACHE_PERSON"];
    [_fetchedResultsController setDelegate:self];
    
    NSError *error = nil;
    if(![_fetchedResultsController performFetch:&error]) {
        NSLog(@"### [PersonVC fetchedResultsController] Error:%@, %@", error, [error userInfo]);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Person *person = (Person *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@", [person firstName], [person lastName]];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
}


@end
