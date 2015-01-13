//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AutoLayout.h"
#import "Person.h"

@interface MyViewController() <UITableViewDataSource>
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) UIBarButtonItem *buttonRequest, *buttonRight;
@property (nonatomic, strong) NSMutableArray *persons;

@end

@implementation MyViewController
@synthesize persons = _persons;

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonRequest = [[UIBarButtonItem alloc] initWithTitle:@"Request" style:UIBarButtonItemStylePlain target:self action:@selector(actionRequest:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonRequest];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Persons"];
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
    [self.tableView setTranslatesAutoresizingMaskIntoConstraints:NO];
    [self.tableView setDataSource:self];
    [self.view addSubview:self.tableView];
    [AutoLayout fill:self.tableView inside:self.view];
    
    _persons = [[NSMutableArray alloc] init];
}

-(void)actionRequest:(id)sender {
    NSLog(@"[MyViewController actionLeft]");
    
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.persons count];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if(!cell) cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
    
    Person *person = (Person *)self.persons[indexPath.row];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@", [person firstName], [person lastName]];
    
    return cell;
}

@end
